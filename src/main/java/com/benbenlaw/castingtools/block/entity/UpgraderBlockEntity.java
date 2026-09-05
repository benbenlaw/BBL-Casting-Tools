package com.benbenlaw.castingtools.block.entity;

import com.benbenlaw.casting.block.entity.FluidAccepting;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.FluidMoverItem;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.castingtools.block.CTBlockEntities;
import com.benbenlaw.castingtools.screen.UpgraderMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.SyncableFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UpgraderBlockEntity extends SyncableBlockEntity implements MenuProvider, FluidAccepting {

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, 3, (i, stack) -> true, i -> i == 2);
    private final SyncableFluidHandler fluidInventory = new SyncableFluidHandler(this, 2, 16000, (i, stack) -> {
        if (i == 0) {
            TagKey<Fluid> experienceTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", "experience"));
            return stack.is(experienceTag);
        }
        if (i == 1) {
            TagKey<Fluid> experienceTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", "experience"));
            return !stack.is(experienceTag);
        };
        return false;
    }, i -> i == 1);

    public UpgraderBlockEntity(BlockPos pos, BlockState state) {
        super(CTBlockEntities.UPGRADER_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(worldPosition).move(0, 1, 0));
        Fluid expFluid = BuiltInRegistries.FLUID.getValue(Identifier.fromNamespaceAndPath("casting", "molten_experience"));
        FluidResource expResource = FluidResource.of(expFluid);

        for (Player player : players) {
            if (player.totalExperience > 0 && fluidInventory.getAmountAsInt(0) < fluidInventory.getCapacityAsInt(0, expResource)) {

                int xpToDrain = 10;
                int fluidAmount = 250;
                if (player.totalExperience < xpToDrain) continue;

                try (Transaction tx = Transaction.openRoot()) {
                    int accepted = fluidInventory.insert(expResource, fluidAmount, tx);

                    if (accepted >= fluidAmount) {
                        tx.commit();
                        player.giveExperiencePoints(-xpToDrain);
                        this.setChanged();
                    }
                }
            }
        }
    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof FluidMoverItem) {
            return FluidMoverItem.onBlockInteract(stack, fluidInventory, new int[]{1, 0}, new int[]{1, 0});
        }

        try (Transaction tx = Transaction.openRoot()) {
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, fluidInventory, tx);
            if (result) {
                tx.commit();
            }
            return result;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        inventory.serialize(output.child("inventory"));
        fluidInventory.serialize(output.child("fluidInventory"));

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        inventory.deserialize(input.childOrEmpty("inventory"));
        fluidInventory.deserialize(input.childOrEmpty("fluidInventory"));

        super.loadAdditional(input);
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    public ItemStacksResourceHandler getAutomationItemHandler() {
        return new ItemStacksResourceHandler(3) {
            @Override
            public int extract(int index, ItemResource resource, int amount, TransactionContext tx) {
                return 0;
            }

            @Override
            public int insert(int index, ItemResource resource, int amount, TransactionContext tx) {
                if (index == 2) return 0;
                return inventory.insert(index, resource, amount, tx);
            }
        };
    }

    public FluidStacksResourceHandler getFluidHandler() {
        return fluidInventory;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, @NonNull Inventory inventory, @NonNull Player player) {
        return new UpgraderMenu(container, inventory, this.worldPosition, new SimpleContainerData(0));
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.castingtools.upgrader");
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {

        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            items.add(ItemUtil.getStack(inventory, i));
        }

        Container tempContainer = new SimpleContainer(items.getFirst(), items.getLast());
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, tempContainer);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(fluidInventory));
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            component.applyToHandlers(fluidInventory);
        }
    }

    @Override
    public SyncableFluidHandler receivingHandler() {
        return fluidInventory;
    }

    @Override
    public int[] acceptingTanks() {
        return new int[] {0, 1};
    }
}