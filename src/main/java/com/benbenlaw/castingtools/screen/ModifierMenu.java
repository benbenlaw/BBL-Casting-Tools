package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.block.entity.ModifierBlockEntity;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.screen.util.ModifierResultSlot;
import com.benbenlaw.castingtools.utils.CTTags;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.benbenlaw.core.block.entity.handler.fluid.SyncableFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;

public class ModifierMenu extends SimpleAbstractContainerMenu {

    protected ModifierBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public ModifierMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(0));
    }

    public ModifierMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CastingToolsMenuTypes.MODIFIER_MENU.get(), containerID, inventory, blockPos, 3);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (ModifierBlockEntity) this.level.getBlockEntity(blockPos);

        assert blockEntity != null;

        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 0, 63, 35));
        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 1, 98, 35).size(1));
        this.addSlot(new ModifierResultSlot((SyncableItemHandler) blockEntity.getItemHandler(), (SyncableFluidHandler) blockEntity.getFluidHandler(), (SyncableItemHandler) blockEntity.getItemHandler(),
                blockEntity.getItemHandler()::set, 2, 152, 35).size(1));

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

        if (toolStack.isEmpty()) {
            if (!currentOutput.isEmpty()) {
                blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                this.broadcastChanges();
            }
            return;
        }

        Modifier modifier = ModifierUtils.getMatchingModifier(toolStack, ingredientStack, FluidUtil.getStack(blockEntity.getFluidHandler(), 1));

        if (modifier == null || !modifier.isValid(toolStack)) {
            if (!currentOutput.isEmpty()) {
                blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                this.broadcastChanges();
            }
            return;
        }

        int requiredExperience = modifier.getExperienceCost();
        FluidStack expFluid = FluidUtil.getStack(blockEntity.getFluidHandler(), 0);
        TagKey<Fluid> experienceTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", "experience"));
        boolean hasExperience = (requiredExperience <= 0) ||
                (!expFluid.isEmpty() && expFluid.is(experienceTag) && expFluid.getAmount() >= requiredExperience);

        boolean hasFluidIngredient;
        var fluidIngOpt = modifier.getFluidIngredient();
        if (fluidIngOpt.isPresent()) {
            var sizedFluidIng = fluidIngOpt.get();
            FluidStack tankFluid = FluidUtil.getStack(blockEntity.getFluidHandler(), 1);
            hasFluidIngredient = sizedFluidIng.test(tankFluid) && tankFluid.getAmount() >= sizedFluidIng.amount();
        } else {
            hasFluidIngredient = true;
        }

        boolean hasItemIngredient;
        var itemIngOpt = modifier.getIngredient();
        if (itemIngOpt.isPresent()) {
            var sizedIng = itemIngOpt.get();
            hasItemIngredient = sizedIng.test(ingredientStack) && ingredientStack.getCount() >= sizedIng.count();
        } else {
            hasItemIngredient = true;
        }

        //Validation checks
        if (hasExperience && hasFluidIngredient && hasItemIngredient) {

            if (ModifierUtils.hasConflict(toolStack, modifier)) {
                ItemStack barrier = new ItemStack(Items.BARRIER);
                barrier.set(DataComponents.CUSTOM_NAME, Component.literal("Incompatible Modifier")
                        .withStyle(ChatFormatting.RED));

                if (!ItemStack.matches(currentOutput, barrier)) {
                    blockEntity.getItemHandler().set(2, ItemResource.of(barrier), 1);
                    this.broadcastChanges();
                }
                return;
            }


            int currentLevel = ModifierUtils.getModifierLevel(toolStack, modifier);
            int maxLevel = getEffectiveMaxLevel(toolStack, modifier);

            if (currentLevel >= maxLevel) {
                ItemStack barrier = new ItemStack(Items.BARRIER);
                barrier.set(DataComponents.CUSTOM_NAME, Component.literal("Max Level Reached")
                        .withStyle(ChatFormatting.RED));

                if (!ItemStack.matches(currentOutput, barrier)) {
                    blockEntity.getItemHandler().set(2, ItemResource.of(barrier), 1);
                    this.broadcastChanges();
                }
                return;
            }

            ItemStack result = toolStack.copy();
            ModifierUtils.setModifierLevel(result, modifier, currentLevel + 1);

            if (!ItemStack.matches(currentOutput, result)) {
                blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                blockEntity.getItemHandler().set(2, ItemResource.of(result), 1);
                this.broadcastChanges();
                blockEntity.setChanged();
            }
        } else {
            if (!currentOutput.isEmpty()) {
                blockEntity.getItemHandler().set(2, ItemResource.EMPTY, 0);
                this.broadcastChanges();
            }
        }
    }

    public static int getEffectiveMaxLevel(ItemStack stack, Modifier modifier) {
        return stack.is(CTTags.Items.ENHANCED)
                ? modifier.getMaxEnhancedLevel()
                : modifier.getMaxLevel();
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

}
