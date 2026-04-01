package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
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
    private final Supplier<Float> damagePerLevel;
    private final Supplier<Integer> maxLevel;

    public SharpnessModifier(Supplier<Float> damagePerLevel, Supplier<Integer> maxLevel) {
        this.damagePerLevel = damagePerLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    public void onPreHit(LivingDamageEvent.Pre event, int level) {
        int effectiveLevel = Math.min(level, maxLevel.get());
        if (effectiveLevel <= 0) return;

        float bonus = effectiveLevel * damagePerLevel.get();
        System.out.println(event.getOriginalDamage() + bonus);
        event.setNewDamage(event.getOriginalDamage() + bonus);
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                ItemTags.SWORDS,
                ItemTags.AXES
        );
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_quartz")), 5000));

    }
}