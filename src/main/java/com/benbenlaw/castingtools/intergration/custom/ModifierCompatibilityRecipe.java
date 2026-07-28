package com.benbenlaw.castingtools.intergration.custom;

import com.benbenlaw.castingtools.modifier.Modifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ModifierCompatibilityRecipe(Modifier modifier, List<ItemStack> compatibleItems) {
}