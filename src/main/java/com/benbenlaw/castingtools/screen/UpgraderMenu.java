package com.benbenlaw.castingtools.screen;

import com.benbenlaw.casting.recipe.custom.MeltingRecipe;
import com.benbenlaw.castingtools.block.entity.UpgraderBlockEntity;
import com.benbenlaw.castingtools.config.CTServerConfig;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.screen.util.UpgraderResultSlot;
import com.benbenlaw.core.block.entity.handler.fluid.SyncableFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpgraderMenu extends SimpleAbstractContainerMenu {

    protected UpgraderBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public UpgraderMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(0));
    }

    public UpgraderMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CTMenuTypes.UPGRADER_MENU.get(), containerID, inventory, blockPos, 3);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (UpgraderBlockEntity) this.level.getBlockEntity(blockPos);

        assert blockEntity != null;

        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 0, 63, 35));
        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 1, 98, 35).size(1));
        this.addSlot(new UpgraderResultSlot((SyncableItemHandler) blockEntity.getItemHandler(), (SyncableFluidHandler) blockEntity.getFluidHandler(),
                (SyncableItemHandler) blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 2, 152, 35, this.level).size(1));

        addDataSlots(data);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        setupResult();
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        setupResult();
    }

    private void setupResult() {
        if (this.level.isClientSide()) return;

        ItemStack ingredientStack = ItemUtil.getStack(blockEntity.getItemHandler(), 0);
        ItemStack toolStack = ItemUtil.getStack(blockEntity.getItemHandler(), 1);
        ItemStack currentOutput = ItemUtil.getStack(blockEntity.getItemHandler(), 2);
        FluidStack tankFluid = FluidUtil.getStack(blockEntity.getFluidHandler(), 1);
        FluidStack expFluid = FluidUtil.getStack(blockEntity.getFluidHandler(), 0);

        if (toolStack.isEmpty()) {
            if (!currentOutput.isEmpty()) {
                blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                this.broadcastChanges();
            }
            return;
        }

        UpgradeResult upgradeResult = findUpgradeMatch(level, toolStack, ingredientStack, tankFluid, expFluid);

        switch (upgradeResult.status()) {
            case NOT_ENOUGH_EXPERIENCE -> {
                ItemStack barrier = new ItemStack(Items.BARRIER);
                barrier.set(DataComponents.CUSTOM_NAME, Component.translatable("tooltip.castingtools.upgrader.not_enough_experience")
                        .withStyle(ChatFormatting.RED));
                if (!ItemStack.matches(currentOutput, barrier)) {
                    blockEntity.getItemHandler().set(2, ItemResource.of(barrier), 1);
                    this.broadcastChanges();
                }
                return;
            }
            case NOT_ENOUGH_MATERIAL -> {
                ItemStack barrier = new ItemStack(Items.BARRIER);
                barrier.set(DataComponents.CUSTOM_NAME, Component.translatable("tooltip.castingtools.upgrader.not_enough_materials")
                        .withStyle(ChatFormatting.RED));
                if (!ItemStack.matches(currentOutput, barrier)) {
                    blockEntity.getItemHandler().set(2, ItemResource.of(barrier), 1);
                    this.broadcastChanges();
                }
                return;
            }
            case NO_RECIPE -> {
                if (!currentOutput.isEmpty()) {
                    blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                    this.broadcastChanges();
                }
                return;
            }
            case SUCCESS -> { /* fall through below */ }
        }

        ItemStack result = upgradeResult.match().get().result().copy();

        DataComponentPatch toolPatch = toolStack.getComponentsPatch();
        result.applyComponents(toolPatch);

        if (toolStack.isDamageableItem() && result.isDamageableItem()) {
            float damagePercent = (float) toolStack.getDamageValue() / toolStack.getMaxDamage();
            int newDamage = Math.round(damagePercent * result.getMaxDamage());
            result.setDamageValue(newDamage);
        } else {
            result.remove(DataComponents.DAMAGE);
        }

        ModifierComponent modifierComponent = toolStack.get(CTDataComponent.MODIFIER_COMPONENT);
        if (modifierComponent != null) {
            result.set(CTDataComponent.MODIFIER_COMPONENT, modifierComponent);
        }

        if (!ItemStack.matches(currentOutput, result)) {
            blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
            blockEntity.getItemHandler().set(2, ItemResource.of(result), 1);
            this.broadcastChanges();
            blockEntity.setChanged();
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        ItemStack stack = super.quickMoveStack(player, index);
        setupResult();
        return stack;
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        super.clicked(slotIndex, buttonNum, containerInput, player);
        if (!this.level.isClientSide()) {
            setupResult();
        }
    }

    public record UpgradeMatch(ItemStack result, int requiredItemCount, int requiredFluidAmount) {}

    public static UpgradeResult findUpgradeMatch(Level level, ItemStack toolStack, ItemStack ingredientStack, FluidStack tankFluid, FluidStack expFluid) {
        if (toolStack.isEmpty()) return UpgradeResult.fail(UpgradeStatus.NO_RECIPE);

        RecipeManager recipeManager = Objects.requireNonNull(level.getServer()).getRecipeManager();

        Optional<ShapedRecipe> sourceRecipeOpt = recipeManager.getRecipes().stream()
                .map(RecipeHolder::value)
                .filter(r -> r instanceof ShapedRecipe)
                .map(r -> (ShapedRecipe) r)
                .filter(r -> r.assemble(CraftingInput.EMPTY).is(toolStack.getItem()))
                .findFirst();

        if (sourceRecipeOpt.isEmpty()) return UpgradeResult.fail(UpgradeStatus.NO_RECIPE);

        ShapedRecipe sourceRecipe = sourceRecipeOpt.get();
        Repairable repairable = toolStack.get(DataComponents.REPAIRABLE);
        if (repairable == null) return UpgradeResult.fail(UpgradeStatus.NO_RECIPE);

        List<Optional<Ingredient>> pattern = sourceRecipe.getIngredients();
        int width = sourceRecipe.getWidth();
        int height = sourceRecipe.getHeight();

        int materialSlotCount = 0;
        for (Optional<Ingredient> slot : pattern) {
            if (slot.isEmpty()) continue;
            if (slot.get().items().anyMatch(repairable.items()::contains)) {
                materialSlotCount++;
            }
        }
        if (materialSlotCount == 0) return UpgradeResult.fail(UpgradeStatus.NO_RECIPE);

        ItemStack newMaterial;
        int requiredItemCount = 0;
        int requiredFluidAmount = 0;

        if (!ingredientStack.isEmpty() && ingredientStack.getCount() >= materialSlotCount) {
            newMaterial = ingredientStack.copyWithCount(1);
            requiredItemCount = materialSlotCount;
        } else if (!tankFluid.isEmpty()) {
            TagKey<Item> gemsTag = Tags.Items.GEMS;
            TagKey<Item> ingotsTag = Tags.Items.INGOTS;

            List<MeltingRecipe> candidates = recipeManager.getRecipes().stream()
                    .map(RecipeHolder::value)
                    .filter(r -> r instanceof MeltingRecipe)
                    .map(r -> (MeltingRecipe) r)
                    .filter(r -> r.output().stream().anyMatch(template -> FluidStack.isSameFluid(tankFluid, template)))
                    .sorted((a, b) -> {
                        boolean aTiered = a.input().ingredient().items().anyMatch(h -> h.is(gemsTag) || h.is(ingotsTag));
                        boolean bTiered = b.input().ingredient().items().anyMatch(h -> h.is(gemsTag) || h.is(ingotsTag));
                        return Boolean.compare(bTiered, aTiered);
                    })
                    .toList();

            ItemStack resolvedMaterial = ItemStack.EMPTY;
            int resolvedFluidAmount = 0;
            boolean foundFluidTypeButNotEnough = false;

            for (MeltingRecipe melting : candidates) {
                Optional<FluidStackTemplate> matchingOutput = melting.output().stream()
                        .filter(template -> FluidStack.isSameFluid(tankFluid, template))
                        .findFirst();

                if (matchingOutput.isEmpty()) continue;

                Optional<Holder<Item>> meltedFromItem = melting.input().ingredient().items().findFirst();
                if (meltedFromItem.isEmpty()) continue;

                int amountPerItem = matchingOutput.get().amount() / melting.input().count();
                int candidateFluidAmount = amountPerItem * materialSlotCount;

                if (tankFluid.getAmount() < candidateFluidAmount) {
                    foundFluidTypeButNotEnough = true;
                    continue;
                }

                ItemStack candidateMaterial = new ItemStack(meltedFromItem.get().value());

                List<ItemStack> trialGrid = new ArrayList<>(width * height);
                for (Optional<Ingredient> slot : pattern) {
                    if (slot.isEmpty()) {
                        trialGrid.add(ItemStack.EMPTY);
                        continue;
                    }
                    Ingredient ing = slot.get();
                    boolean isMaterialSlot = ing.items().anyMatch(repairable.items()::contains);
                    trialGrid.add(isMaterialSlot
                            ? candidateMaterial.copy()
                            : ing.items().findFirst().map(h -> new ItemStack(h.value())).orElse(ItemStack.EMPTY));
                }

                CraftingInput trialInput = CraftingInput.of(width, height, trialGrid);
                Optional<RecipeHolder<CraftingRecipe>> trialMatch = recipeManager
                        .getRecipeFor(RecipeType.CRAFTING, trialInput, level);

                if (trialMatch.isEmpty()) continue;

                ItemStack trialResult = trialMatch.get().value().assemble(trialInput);

                Repairable resultRepairable = trialResult.get(DataComponents.REPAIRABLE);
                if (resultRepairable == null || !resultRepairable.items().contains(meltedFromItem.get())) continue;

                resolvedMaterial = candidateMaterial;
                resolvedFluidAmount = candidateFluidAmount;
                break;
            }

            if (resolvedMaterial.isEmpty()) {
                return foundFluidTypeButNotEnough
                        ? UpgradeResult.fail(UpgradeStatus.NOT_ENOUGH_MATERIAL)
                        : UpgradeResult.fail(UpgradeStatus.NO_RECIPE);
            }

            newMaterial = resolvedMaterial;
            requiredFluidAmount = resolvedFluidAmount;
        } else {
            return UpgradeResult.fail(UpgradeStatus.NOT_ENOUGH_MATERIAL);
        }

        List<ItemStack> newGrid = new ArrayList<>(width * height);
        for (Optional<Ingredient> slot : pattern) {
            if (slot.isEmpty()) {
                newGrid.add(ItemStack.EMPTY);
                continue;
            }
            Ingredient ing = slot.get();
            boolean isMaterialSlot = ing.items().anyMatch(repairable.items()::contains);
            newGrid.add(isMaterialSlot
                    ? newMaterial.copy()
                    : ing.items().findFirst().map(h -> new ItemStack(h.value())).orElse(ItemStack.EMPTY));
        }

        CraftingInput syntheticInput = CraftingInput.of(width, height, newGrid);
        Optional<RecipeHolder<CraftingRecipe>> match = recipeManager
                .getRecipeFor(RecipeType.CRAFTING, syntheticInput, level);

        if (match.isEmpty()) return UpgradeResult.fail(UpgradeStatus.NO_RECIPE);

        int expCost = CTServerConfig.costForUpgrader.get();
        if (expFluid.isEmpty() || expFluid.getAmount() < expCost) {
            return UpgradeResult.fail(UpgradeStatus.NOT_ENOUGH_EXPERIENCE);
        }

        ItemStack result = match.get().value().assemble(syntheticInput);
        return UpgradeResult.success(new UpgradeMatch(result, requiredItemCount, requiredFluidAmount));
    }

    public enum UpgradeStatus {
        SUCCESS,
        NO_RECIPE,
        NOT_ENOUGH_EXPERIENCE,
        NOT_ENOUGH_MATERIAL
    }

    public record UpgradeResult(UpgradeStatus status, Optional<UpgraderMenu.UpgradeMatch> match) {
        public static UpgradeResult fail(UpgradeStatus status) {
            return new UpgradeResult(status, Optional.empty());
        }
        public static UpgradeResult success(UpgraderMenu.UpgradeMatch match) {
            return new UpgradeResult(UpgradeStatus.SUCCESS, Optional.of(match));
        }
    }
}