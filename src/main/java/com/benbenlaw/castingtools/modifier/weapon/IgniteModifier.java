package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
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
    private final Supplier<Integer> durationMultiplier;
    private final Supplier<Integer> maxLevel;

    public IgniteModifier(Supplier<Integer> durationMultiplier, Supplier<Integer> maxLevel) {
        this.durationMultiplier = durationMultiplier;
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public void onPostHit(LivingDamageEvent.Post event, int level) {
        int effectiveLevel = Math.min(level, maxLevel.get());
        event.getEntity().setRemainingFireTicks(effectiveLevel * durationMultiplier.get());
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                CastingToolsTags.Items.ALL_MELEE_WEAPONS
        );
    }

    @Override
    public Set<Item> getValidItems() {
        return Set.of(
                Items.STICK
        );
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Identifier.withDefaultNamespace("lava")), 8000));
    }

    @Override
    public Optional<SizedIngredient> getIngredient() {
        return Optional.of(SizedIngredient.of(Items.FLINT_AND_STEEL, 1));
    }
}