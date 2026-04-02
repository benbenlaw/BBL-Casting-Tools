package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
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
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;

public class SilkTouchModifier extends Modifier {

    @Override
    public void onCalculateDrops(ItemStack fakeStack, int level, Level world) {
        if (level > 0) {
            fakeStack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.SILK_TOUCH), 1);
        }
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_emerald")), 720));
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
    public Set<Modifier> getIncompatibleModifiers() {
        return Set.of(
                ModifierRegistry.FORTUNE.get()
        );
    }
}