package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class KnockbackModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {

        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getDirectEntity();
        if (attacker == null) return;

        float baseKnockback = data.additionalValue().orElse(0.5).floatValue();
        float strength = baseKnockback * toolLevel;

        double xDiff = target.getX() - attacker.getX();
        double zDiff = target.getZ() - attacker.getZ();
        
        if (xDiff * xDiff + zDiff * zDiff > 0) {
            target.knockback(strength, -xDiff, -zDiff);
        }
    }
}