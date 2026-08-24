package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CTBlocks;
import com.benbenlaw.castingtools.event.client.ClientRecipeCache;
import com.benbenlaw.castingtools.intergration.custom.ModifierCompatibilityRecipe;
import com.benbenlaw.castingtools.intergration.custom.ModifierRecipe;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.screen.ModifierScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.registration.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEICastingToolsPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    @Override
    public @NotNull Identifier getPluginUid() {
        return CastingTools.identifier("jei_plugin");
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAlias(CTBlocks.MODIFIER.toStack(), "Equipment Modifier");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ModifierRecipeCategory.RECIPE_TYPE, CTBlocks.MODIFIER.toStack());
        registration.addCraftingStation(BeheadingRecipeCategory.RECIPE_TYPE, CTBlocks.MODIFIER.toStack());
        registration.addCraftingStation(TreasureRecipeCategory.RECIPE_TYPE, CTBlocks.MODIFIER.toStack());
        registration.addCraftingStation(PulverizingRecipeCategory.RECIPE_TYPE, CTBlocks.MODIFIER.toStack());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();

        registration.addRecipeCategories(new ModifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BeheadingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TreasureRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PulverizingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new ModifierCompatibilityRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ModifierRecipe> recipes = new ArrayList<>();

        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (modifier.getData() != null) {
                recipes.add(new ModifierRecipe(modifier));
            }
        }

        registration.addRecipes(ModifierRecipeCategory.RECIPE_TYPE, recipes);

        registration.addIngredientInfo(new ItemStack(CTBlocks.MODIFIER), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.castingtools.modifier.information"));

        registration.addRecipes(BeheadingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedBeheadingRecipes().stream().toList());
        registration.addRecipes(TreasureRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedTreasureRecipes().stream().toList());
        registration.addRecipes(PulverizingRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedPulverizingRecipes().stream().toList());

        List<ModifierCompatibilityRecipe> compatibilityRecipes = new ArrayList<>();

        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (modifier.getData() == null) continue;

            Map<Item, ItemStack> compatibleByItem = new LinkedHashMap<>();

            for (TagKey<Item> tag : modifier.getValidTags()) {
                BuiltInRegistries.ITEM.get(tag).ifPresent(holders -> {
                    for (Holder<Item> holder : holders) {
                        compatibleByItem.putIfAbsent(holder.value(), new ItemStack(holder.value()));
                    }
                });
            }

            for (Item item : modifier.getValidItems()) {
                compatibleByItem.putIfAbsent(item, new ItemStack(item));
            }

            if (!compatibleByItem.isEmpty()) {
                compatibilityRecipes.add(new ModifierCompatibilityRecipe(modifier, new ArrayList<>(compatibleByItem.values())));
            }
        }

        registration.addRecipes(ModifierCompatibilityRecipeCategory.RECIPE_TYPE, compatibilityRecipes);

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        List<Modifier> allModifiers = new ArrayList<>();
        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (modifier.getData() != null) {
                allModifiers.add(modifier);
            }
        }
        registration.register(ModifierIngredientType.INSTANCE, allModifiers, new ModifierIngredientHelper(), new ModifierIngredientRenderer(), ModifierRegistry.MODIFIER_REGISTRY.byNameCodec());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ModifierScreen.class, 121, 34, 24, 16, ModifierRecipeCategory.RECIPE_TYPE);
    }
}

