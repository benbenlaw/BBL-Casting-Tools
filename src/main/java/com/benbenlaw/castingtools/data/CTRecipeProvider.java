package com.benbenlaw.castingtools.data;

import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.custom.MeltingRecipeBuilder;
import com.benbenlaw.casting.data.custom.MixingRecipeBuilder;
import com.benbenlaw.casting.data.custom.SolidifierRecipeBuilder;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.fluids.CTFluids;
import com.benbenlaw.castingtools.item.CTItems;
import com.benbenlaw.castingtools.utils.CTTags;
import com.benbenlaw.core.tag.ResourceType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.casting.data.custom.FluidStackTemplateHelper.getFluidIngredient;

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
        shapeless(RecipeCategory.MISC, CastingToolsBlocks.MODIFIER).requires(CastingToolsBlocks.MODIFIER).unlockedBy("has_modifier", has(CastingToolsBlocks.MODIFIER)).save(output, "castingtools:reset");

        //Modifier
        shaped(RecipeCategory.MISC, CastingToolsBlocks.MODIFIER)
                .pattern("AAA")
                .pattern("B B")
                .pattern("AAA")
                .define('A', CastingBlocks.BLACK_BRICKS)
                .define('B', CastingBlocks.SOLIDIFIER)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output);

        //Omnithium Template
        shaped(RecipeCategory.MISC, CTItems.OMNITHIUM_UPGRADE_SMITHING_TEMPLATE)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("AAA")
                .define('A', CTItems.OMNITHIUM_INGOT)
                .define('B', CTItems.OMNITHIUM_UPGRADE_SMITHING_TEMPLATE)
                .define('C', CastingToolsBlocks.OMNITHIUM_BLOCK)
                .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
                .save(output);

        //Smithing
        omnithiumSmithing(Items.DIAMOND_PICKAXE, CTItems.OMNITHIUM_PICKAXE.get());
        omnithiumSmithing(Items.DIAMOND_SHOVEL, CTItems.OMNITHIUM_SHOVEL.get());
        omnithiumSmithing(Items.DIAMOND_AXE, CTItems.OMNITHIUM_AXE.get());
        omnithiumSmithing(Items.DIAMOND_HOE, CTItems.OMNITHIUM_HOE.get());
        omnithiumSmithing(Items.DIAMOND_SWORD, CTItems.OMNITHIUM_SWORD.get());
        omnithiumSmithing(Items.DIAMOND_SPEAR, CTItems.OMNITHIUM_SPEAR.get());
        omnithiumSmithing(Items.DIAMOND_HELMET, CTItems.OMNITHIUM_HELMET.get());
        omnithiumSmithing(Items.DIAMOND_CHESTPLATE, CTItems.OMNITHIUM_CHESTPLATE.get());
        omnithiumSmithing(Items.DIAMOND_LEGGINGS, CTItems.OMNITHIUM_LEGGINGS.get());
        omnithiumSmithing(Items.DIAMOND_BOOTS, CTItems.OMNITHIUM_BOOTS.get());
        omnithiumSmithing(Items.BOW, CTItems.OMNITHIUM_BOW.get());
        omnithiumSmithing(Items.CROSSBOW, CTItems.OMNITHIUM_CROSSBOW.get());

        //Omnithium
        alloyMixingRecipes("omnithium", new FluidStackTemplate(CTFluids.MOLTEN_OMNITHIUM.getFluid(), 90),
                List.of(
                        getFluidIngredient("molten_end_stone", 8000),
                        getFluidIngredient("molten_netherite", 360),
                        getFluidIngredient("molten_steel", 3240),
                        getFluidIngredient("molten_experience", 8000)
                ));

        simpleMeltingRecipe(List.of(new FluidStackTemplate(CTFluids.MOLTEN_OMNITHIUM.getFluid(), 810)), CastingToolsBlocks.OMNITHIUM_BLOCK,
                "omnithium/block", ResourceType.STORAGE_BLOCKS, 1400);

        simpleMeltingRecipe(List.of(new FluidStackTemplate(CTFluids.MOLTEN_OMNITHIUM.getFluid(), 90)), CTItems.OMNITHIUM_INGOT,
                "omnithium/ingot", ResourceType.INGOTS, 1400);

        simpleMeltingRecipe(List.of(new FluidStackTemplate(CTFluids.MOLTEN_OMNITHIUM.getFluid(), 10)), CTItems.OMNITHIUM_NUGGET,
                "omnithium/nugget", ResourceType.INGOTS, 1400);

        simpleSolidifierRecipe(CastingToolsBlocks.OMNITHIUM_BLOCK, new SizedFluidIngredient(FluidIngredient.of(CTFluids.MOLTEN_OMNITHIUM.getFluid().getSource()), 810),
                CastingItems.BLOCK_MOLD, "omnithium/block", ResourceType.STORAGE_BLOCKS, 1400);

        simpleSolidifierRecipe(CTItems.OMNITHIUM_INGOT, new SizedFluidIngredient(FluidIngredient.of(CTFluids.MOLTEN_OMNITHIUM.getFluid().getSource()), 90),
                CastingItems.INGOT_MOLD, "omnithium/ingot", ResourceType.INGOTS, 1400);

        simpleSolidifierRecipe(CTItems.OMNITHIUM_NUGGET, new SizedFluidIngredient(FluidIngredient.of(CTFluids.MOLTEN_OMNITHIUM.getFluid().getSource()), 10),
                CastingItems.NUGGET_MOLD, "omnithium/nugget", ResourceType.INGOTS, 1400);

        nineBlockStorageRecipes(RecipeCategory.MISC, CTItems.OMNITHIUM_INGOT, RecipeCategory.MISC, CastingToolsBlocks.OMNITHIUM_BLOCK, "omnithium/block_from_ingots", "omnithium", "omnithium/ingots_from_block", "omnithium");
        nineBlockStorageRecipes(RecipeCategory.MISC, CTItems.OMNITHIUM_NUGGET, RecipeCategory.MISC, CTItems.OMNITHIUM_INGOT, "omnithium/ingot_from_nuggets", "omnithium", "omnithium/nuggets_from_ingot", "omnithium");


    }

    protected void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String packingRecipeId, @Nullable String packingRecipeGroup, String unpackingRecipeId, @Nullable String unpackingRecipeGroup) {
        this.shapeless(unpackedFormCategory, unpackedForm, 9).requires(packedForm).group(unpackingRecipeGroup).unlockedBy(getHasName(packedForm), this.has(packedForm)).save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(unpackingRecipeId)));
        this.shaped(packedFormCategory, packedForm).define('#', unpackedForm).pattern("###").pattern("###").pattern("###").group(packingRecipeGroup).unlockedBy(getHasName(unpackedForm), this.has(unpackedForm)).save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(packingRecipeId)));
    }

    private void alloyMixingRecipes(String material, FluidStackTemplate outputFluid, List<SizedFluidIngredient> inputFluids) {

        NonNullList<SizedFluidIngredient> inputs = NonNullList.create();
        inputs.addAll(inputFluids);

        MixingRecipeBuilder.mixingRecipesBuilder(
                        inputs,
                        outputFluid)
                .unlockedBy("has_mixer", has(CastingBlocks.MIXER))
                .save(output, material + "alloy");
    }

    public void simpleMeltingRecipe(List<FluidStackTemplate> outputs, ItemLike input, String id, ResourceType resourceType, int temp) {
        MeltingRecipeBuilder.meltingRecipesBuilder(
                SizedIngredient.of(input, 1),
                outputs,
                temp,
                Optional.of(getDurationModifier(resourceType))).save(output, id);
    }

    public void simpleSolidifierRecipe(ItemLike block, SizedFluidIngredient fluidStack, ItemLike mold, String id, ResourceType resourceType, int temp) {
        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                SizedIngredient.of(mold, 1),
                SizedIngredient.of(block.asItem(), 1),
                fluidStack,
                temp,
                Optional.of(getDurationModifier(resourceType))).save(output, id);
    }

    private double getDurationModifier(ResourceType type) {
        return switch (type) {
            case NUGGETS -> 0.2;
            case RODS, WIRES -> 0.4;
            case INGOTS, PLATES, DUSTS, GEMS -> 0.5;
            case GEARS -> 1.2;
            case STORAGE_BLOCKS -> 2.5;
            case ORES -> 1.5;
            case RAW_MATERIALS -> 1.25;
            case RAW_STORAGE_BLOCKS -> 3.0;
            default -> 1.0;
        };
    }

    protected void omnithiumSmithing(Item base, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(CTItems.OMNITHIUM_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(base), this.tag(CTTags.Items.OMNITHIUM_INGOT), RecipeCategory.MISC, result).unlocks("has_omnithium_ingot", this.has(CTTags.Items.OMNITHIUM_INGOT)).save(this.output, getItemName(result) + "_smithing");
    }

}
