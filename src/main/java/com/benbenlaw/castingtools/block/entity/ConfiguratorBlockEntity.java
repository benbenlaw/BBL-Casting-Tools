package com.benbenlaw.castingtools.block.entity;

import com.benbenlaw.castingtools.block.CTBlockEntities;
import com.benbenlaw.castingtools.screen.ConfiguratorMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class ConfiguratorBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, 1, (i, stack) -> true, i -> true);

    public ConfiguratorBlockEntity(BlockPos pos, BlockState state) {
        super(CTBlockEntities.CONFIGURATOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        inventory.serialize(output.child("inventory"));
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        inventory.deserialize(input.childOrEmpty("inventory"));
        super.loadAdditional(input);
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, @NonNull Inventory inventory, @NonNull Player player) {
        return new ConfiguratorMenu(container, inventory, this.worldPosition, new SimpleContainerData(0));
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.castingtools.configurator");
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, new SimpleContainer(ItemUtil.getStack(inventory, 0)));
    }
}
