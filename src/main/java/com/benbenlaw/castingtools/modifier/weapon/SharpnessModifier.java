package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class SharpnessModifier extends Modifier {

    @Override
    public void onPreHit(LivingDamageEvent.Pre event, ModifierData data, int toolLevel) {

        int effectiveLevel = Math.min(toolLevel, data.maxLevel());
        if (effectiveLevel <= 0) return;

        float bonus = effectiveLevel * data.additionalValue().orElse(1.0).floatValue();
        System.out.println(event.getOriginalDamage() + bonus);
        event.setNewDamage(event.getOriginalDamage() + bonus);
    }


}