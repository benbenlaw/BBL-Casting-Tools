package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class IgniteModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {
        int effectiveLevel = Math.min(toolLevel, data.maxLevel());
        event.getEntity().setRemainingFireTicks(effectiveLevel * data.additionalValue().orElse(20.0).intValue());
    }
}