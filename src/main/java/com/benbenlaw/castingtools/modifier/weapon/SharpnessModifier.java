package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

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