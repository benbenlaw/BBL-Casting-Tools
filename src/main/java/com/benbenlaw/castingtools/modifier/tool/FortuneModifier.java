package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class FortuneModifier extends Modifier {

    private final Supplier<Integer> maxLevel;

    public FortuneModifier(Supplier<Integer> maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public void onCalculateDrops(ItemStack fakeStack, int level, Level world) {
        int effectiveLevel = Math.min(level, maxLevel.get());
        if (effectiveLevel > 0) {
            fakeStack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.FORTUNE), effectiveLevel);
        }
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                ItemTags.PICKAXES,
                ItemTags.AXES,
                ItemTags.SHOVELS,
                ItemTags.HOES
        );
    }

    @Override
    public Optional<SizedIngredient> getIngredient() {
        return Optional.of(SizedIngredient.of(Items.LAPIS_LAZULI, 12));
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_lapis")), 1350));
    }

    @Override
    public Set<Modifier> getIncompatibleModifiers() {
        return Set.of(
                ModifierRegistry.FORTUNE.get()
        );
    }
}