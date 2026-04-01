package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.modifier.Modifier;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;

public record ModifierRecipe(
        Modifier modifier
) {
}
