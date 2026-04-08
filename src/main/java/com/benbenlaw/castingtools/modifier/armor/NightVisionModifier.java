package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class NightVisionModifier extends Modifier {

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {

        Player player = event.getEntity();
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet != stack) return;
        if (data.additionalValue().isEmpty()) return;
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, data.additionalValue().get().intValue(), 0, false, false));
        if (level.getGameTime() % data.additionalValue().get().intValue() == 0) {
             helmet.hurtAndBreak(1, player, EquipmentSlot.HEAD);
        }
    }
}
