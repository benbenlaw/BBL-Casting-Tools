package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class SpeedModifier extends Modifier {

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {
        if (event.getEntity().level().isClientSide()) return;

        Player player = event.getEntity();

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (stack != boots) return;

        int totalSpeedLevel = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorStack = player.getItemBySlot(slot);

                var comp = armorStack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
                if (comp != null) {
                    Integer level = comp.modifiers().get(ModifierRegistry.SPEED.get().getId());
                    if (level != null) {
                        totalSpeedLevel += level;
                    }
                }
            }
        }

        if (totalSpeedLevel > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, totalSpeedLevel - 1, false, false, true));
        }
    }
}