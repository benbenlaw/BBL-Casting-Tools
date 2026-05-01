package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.*;

public class MagnetModifier extends Modifier {

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getEntity().gameMode() == GameType.SPECTATOR) return;

        Player player = event.getEntity();

        boolean isHeld = stack == player.getMainHandItem() || stack == player.getOffhandItem();
        if (!isHeld) return;

        AABB box = player.getBoundingBox().inflate(toolLevel);

        for (ItemEntity itemEntity : player.level().getEntitiesOfClass(ItemEntity.class, box)) {
            ItemStack entityStack = itemEntity.getItem();

            if (player.getInventory().getFreeSlot() == -1 && !canStackInInventory(player, entityStack)) {
                continue;
            }

            if (player.getInventory().add(entityStack)) {
                player.take(itemEntity, entityStack.getCount());
                itemEntity.discard();
            }
        }
    }

    private boolean canStackInInventory(Player player, ItemStack stack) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (ItemStack.isSameItemSameComponents(slotStack, stack) &&
                    slotStack.getCount() < slotStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }
}
