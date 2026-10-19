package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

public class ExperienceBoostModifier extends Modifier {

    @Override
    public void onBlockDrops(BlockDropsEvent event, ModifierData data, int toolLevel) {
        int originalAmount = event.getDroppedExperience();

        if (originalAmount > 0) {
            int newAmount = (int) (originalAmount * (1 + data.additionalValue().orElse(0.25).floatValue() * toolLevel));
            event.setDroppedExperience(newAmount);
        }
    }

    @Override
    public void onExperienceDropped(LivingExperienceDropEvent event, ModifierData data, int toolLevel) {
        int originalAmount = event.getOriginalExperience();
        if (originalAmount > 0) {
            int newAmount = (int) (originalAmount * (1 + data.additionalValue().orElse(0.25).floatValue() * toolLevel));
            event.setDroppedExperience(newAmount);
        }
    }
}
