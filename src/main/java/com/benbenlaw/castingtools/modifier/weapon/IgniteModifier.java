package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class IgniteModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {
        int effectiveLevel = Math.min(toolLevel, data.maxLevel());
        event.getEntity().setRemainingFireTicks(effectiveLevel * data.additionalValue().orElse(20.0).intValue());
    }
}