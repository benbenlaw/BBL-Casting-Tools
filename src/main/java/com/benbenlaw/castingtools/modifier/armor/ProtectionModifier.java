package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ProtectionModifier extends Modifier {
    @Override
    public void onPreHit(LivingDamageEvent.Pre event, ModifierData data, int toolLevel) {
        float currentDamage = event.getNewDamage();

        double reductionPerLevel = data.additionalValue().orElse(0.035);
        float multiplier = (float) (1.0 - (reductionPerLevel * toolLevel));

        event.setNewDamage(currentDamage * multiplier);
    }
}