package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.screen.ControllerScreen;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlockEntities;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.screen.ModifierScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.registration.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEICastingToolsPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    @Override
    public @NotNull Identifier getPluginUid() {
        return CastingTools.identifier("jei_plugin");
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAlias(CastingToolsBlocks.MODIFIER.toStack(), "Equipment Modifier");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ModifierRecipeCategory.RECIPE_TYPE, CastingToolsBlocks.MODIFIER.toStack());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();

        registration.addRecipeCategories(new ModifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ModifierRecipe> recipes = new ArrayList<>();

        for (Modifier modifier : ModifierRegistry.REGISTRY) {
            recipes.add(new ModifierRecipe(modifier));
        }

        registration.addRecipes(ModifierRecipeCategory.RECIPE_TYPE, recipes);

        registration.addIngredientInfo(new ItemStack(CastingToolsBlocks.MODIFIER), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.castingtools.modifier.information"));

    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(ModifierScreen.class, 121, 34, 24, 16, ModifierRecipeCategory.RECIPE_TYPE);

    }
}

