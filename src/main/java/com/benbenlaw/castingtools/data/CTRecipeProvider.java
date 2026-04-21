package com.benbenlaw.castingtools.data;

import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CTRecipeProvider extends RecipeProvider {

    public CTRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new CTRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return CastingTools.MOD_ID + " Recipes";
        }
    }


    @Override
    protected void buildRecipes() {
        //Reset
        shapeless(RecipeCategory.MISC, CastingToolsBlocks.MODIFIER).requires(CastingToolsBlocks.MODIFIER).unlockedBy("has_modifier", has(CastingToolsBlocks.MODIFIER)).save(output);

        //Modifier
        shaped(RecipeCategory.MISC, CastingToolsBlocks.MODIFIER)
                .pattern("AAA")
                .pattern("B B")
                .pattern("AAA")
                .define('A', CastingBlocks.BLACK_BRICKS)
                .define('B', CastingBlocks.SOLIDIFIER)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output, "castingtools:crafting/modifier");
    }
}
