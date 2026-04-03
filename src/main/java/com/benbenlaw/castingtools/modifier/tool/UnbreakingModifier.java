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

    //All logic for this modifier is handler in the ItemStackMixin, this allows for compatibility with other mods that use hurt and break methods
}