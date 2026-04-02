package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class RepairingModifier extends Modifier {
    private final Supplier<Integer> baseTickAtFirstLevel;
    private final Supplier<Integer> tickReductionPerLevel;
    private final Supplier<Integer> maxLevel;

    public RepairingModifier(Supplier<Integer> baseTickAtFirstLeve, Supplier<Integer> tickReductionPerLevel, Supplier<Integer> maxLevel) {
        this.baseTickAtFirstLevel = baseTickAtFirstLeve;
        this.tickReductionPerLevel = tickReductionPerLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, int level) {
        if (event.getEntity().level().isClientSide()) return;

        if (stack.isDamaged()) {
            long gameTime = event.getEntity().level().getGameTime();

            if (gameTime % getTicksBetweenRepairs(level) == 0) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

    private int getTicksBetweenRepairs(int level) {
        return Math.max(1, baseTickAtFirstLevel.get() - tickReductionPerLevel.get() * level);
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
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_glowstone")), 8000));

    }

    @Override
    public Set<Modifier> getIncompatibleModifiers() {
        return Set.of(
                ModifierRegistry.UNBREAKING.get()
        );
    }
}
