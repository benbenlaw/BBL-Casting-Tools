package com.benbenlaw.castingtools.intergration.custom;

import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record TreasureRecipe(Block block, List<ChanceResult> results) {
}
