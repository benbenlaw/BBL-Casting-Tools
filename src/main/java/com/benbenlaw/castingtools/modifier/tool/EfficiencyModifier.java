package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class EfficiencyModifier extends Modifier {

    @Override
    public void onBreakSpeed(PlayerEvent.BreakSpeed event, ModifierData data, int toolLevel) {
        if (toolLevel <= 0) return;

        if (event.getEntity().getMainHandItem().isCorrectToolForDrops(event.getState())) {
            float bonus = (float) (data.additionalValue().orElse(0.5) * toolLevel);
            event.setNewSpeed(event.getNewSpeed() + bonus);
        }
    }
}
