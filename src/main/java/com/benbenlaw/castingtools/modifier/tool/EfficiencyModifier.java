package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
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
    private final Supplier<Float> effectPerLevel;
    private final Supplier<Integer> maxLevel;

    public EfficiencyModifier(Supplier<Float> effectPerLevel, Supplier<Integer> maxLevel) {
        this.effectPerLevel = effectPerLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public void onBreakSpeed(PlayerEvent.BreakSpeed event, int level) {
        if (level <= 0) return;

        if (event.getEntity().getMainHandItem().isCorrectToolForDrops(event.getState())) {
            float bonus = effectPerLevel.get() * level;
            event.setNewSpeed(event.getNewSpeed() + bonus);
        }
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                CastingToolsTags.Items.ALL_TOOLS
        );
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_redstone")), 1350));

    }
}
