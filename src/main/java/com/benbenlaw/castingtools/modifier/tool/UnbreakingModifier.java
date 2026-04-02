package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class UnbreakingModifier extends Modifier {
    private final Supplier<Float> effectPerLevel;
    private final Supplier<Integer> maxLevel;

    public UnbreakingModifier(Supplier<Float> effectPerLevel, Supplier<Integer> maxLevel) {
        this.effectPerLevel = effectPerLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                CastingToolsTags.Items.ALL_TOOLS,
                CastingToolsTags.Items.ALL_ARMORS,
                CastingToolsTags.Items.ALL_WEAPONS

        );
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_obsidian")), 8000));
    }

    @Override
    public Set<Modifier> getIncompatibleModifiers() {
        return Set.of(
                ModifierRegistry.REPAIRING.get()
        );
    }
}
