package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class LifestealModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        float damageDealt = event.getInflictedDamage();
        double lifestealPercentage = data.additionalValue().orElse(0.1);

        float healAmount = (float) (damageDealt * lifestealPercentage * toolLevel);

        if (healAmount > 0) {
            livingAttacker.heal(healAmount);
        }
    }


}