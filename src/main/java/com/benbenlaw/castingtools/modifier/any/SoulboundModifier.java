package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SoulboundModifier extends Modifier {

    @Override
    public void onPlayerDeath(LivingDeathEvent event, ItemStack stack, ModifierData data, int toolLevel, int slot) {
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, customData ->
                customData.update(tag -> tag.putInt("casting_original_slot", slot))
        );
    }

    @Override
    public void onPlayerDrops(LivingDropsEvent event, ItemStack stack, ModifierData data, int toolLevel, int slot) {
        if (event.getEntity() instanceof Player player) {
            event.getDrops().removeIf(itemEntity -> {
                ItemStack droppedStack = itemEntity.getItem();

                if (droppedStack == stack) {
                    CustomData customData = droppedStack.get(DataComponents.CUSTOM_DATA);
                    if (customData != null && customData.copyTag().contains("casting_original_slot")) {
                        int savedSlot = customData.copyTag().getIntOr("casting_original_slot", 0);

                        player.getInventory().setItem(savedSlot, droppedStack);

                        droppedStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd ->
                                cd.update(tag -> tag.remove("casting_original_slot"))
                        );
                        return true;
                    }
                }
                return false;
            });
        }
    }

    @Override
    public void onPlayerClone(PlayerEvent.Clone event, ModifierData data, int toolLevel) {
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        for (int i = 0; i < oldPlayer.getInventory().getContainerSize(); i++) {
            ItemStack stack = oldPlayer.getInventory().getItem(i);

            if (!stack.isEmpty() && ModifierUtils.hasSoulbound(stack)) {
                newPlayer.getInventory().setItem(i, stack.copy());
            }
        }
    }
}