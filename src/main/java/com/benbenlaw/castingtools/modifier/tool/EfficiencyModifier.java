package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

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
