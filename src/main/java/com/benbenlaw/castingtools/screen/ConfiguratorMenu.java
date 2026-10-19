package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.block.entity.ConfiguratorBlockEntity;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.utils.KeyCombo;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

public class ConfiguratorMenu extends SimpleAbstractContainerMenu {

    public static final int TOOL_SLOT_X = 8;
    public static final int TOOL_SLOT_Y = 35;

    protected ConfiguratorBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public ConfiguratorMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(0));
    }

    public ConfiguratorMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(CTMenuTypes.CONFIGURATOR_MENU.get(), containerID, inventory, blockPos, 1);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.data = data;
        this.blockEntity = (ConfiguratorBlockEntity) this.level.getBlockEntity(blockPos);

        assert blockEntity != null;

        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, 0, TOOL_SLOT_X, TOOL_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT);
                return comp != null && !comp.modifiers().isEmpty();
            }
        }.size(1));

        addDataSlots(data);
    }

    public ItemStack getToolStack() {
        return this.slots.get(36).getItem();
    }

    public void setKeybind(Identifier modifierId, int keyCode) {
        if (this.level.isClientSide()) return;
        if (keyCode < 0 || KeyCombo.key(keyCode) > 512 || KeyCombo.modifiers(keyCode) != (keyCode >> 16)) return;

        ItemStack tool = ItemUtil.getStack(blockEntity.getItemHandler(), 0);
        ModifierComponent comp = tool.get(CTDataComponent.MODIFIER_COMPONENT);
        if (comp == null || !comp.modifiers().containsKey(modifierId)) return;

        ItemStack updated = tool.copy();
        updated.set(CTDataComponent.MODIFIER_COMPONENT, comp.withKeybind(modifierId, keyCode));

        blockEntity.getItemHandler().set(0, ItemResource.of(updated), 1);
        blockEntity.setChanged();
        this.broadcastChanges();
    }

    public void setActiveLevel(Identifier modifierId, int activeLevel) {
        if (this.level.isClientSide()) return;

        ItemStack tool = ItemUtil.getStack(blockEntity.getItemHandler(), 0);
        ModifierComponent comp = tool.get(CTDataComponent.MODIFIER_COMPONENT);
        if (comp == null) return;

        Integer installed = comp.modifiers().get(modifierId);
        if (installed == null) return;

        ItemStack updated = tool.copy();
        updated.set(CTDataComponent.MODIFIER_COMPONENT, comp.withLimit(modifierId, Math.max(0, Math.min(activeLevel, installed))));

        blockEntity.getItemHandler().set(0, ItemResource.of(updated), 1);
        blockEntity.setChanged();
        this.broadcastChanges();
    }
}
