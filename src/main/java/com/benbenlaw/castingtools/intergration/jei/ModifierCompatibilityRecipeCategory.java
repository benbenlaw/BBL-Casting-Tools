package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.intergration.custom.ModifierCompatibilityRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawablesView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModifierCompatibilityRecipeCategory implements IRecipeCategory<ModifierCompatibilityRecipe> {

    public static final IRecipeType<ModifierCompatibilityRecipe> RECIPE_TYPE =
            IRecipeType.create(Casting.identifier("modifier_compatibility"), ModifierCompatibilityRecipe.class);

    private final int width = 160;
    private final int height = 110;
    private final IDrawable icon;

    public ModifierCompatibilityRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingToolsBlocks.MODIFIER.get()));
    }

    @Override
    public @Nullable Identifier getIdentifier(ModifierCompatibilityRecipe recipe) {
        return recipe.modifier().getId();
    }

    @Override
    public @NotNull IRecipeType<ModifierCompatibilityRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.castingtools.modifier_compatability");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ModifierCompatibilityRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .add(ModifierIngredientType.INSTANCE, recipe.modifier()).setBackground(JEICastingToolsPlugin.slotDrawable, -1, -1);

        for (ItemStack stack : sortByPlayerInventory(recipe.compatibleItems())) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 0).add(stack);
        }
    }

    private List<ItemStack> sortByPlayerInventory(List<ItemStack> items) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return items;
        }

        Set<Item> heldItems = new HashSet<>();

        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (!stack.isEmpty()) {
                heldItems.add(stack.getItem());
            }
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipped = player.getItemBySlot(slot);
            if (!equipped.isEmpty()) {
                heldItems.add(equipped.getItem());
            }
        }

        return items.stream()
                .sorted(Comparator.comparing((ItemStack stack) -> !heldItems.contains(stack.getItem())))
                .toList();
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ModifierCompatibilityRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> outputs = recipeSlots.getSlots(RecipeIngredientRole.RENDER_ONLY);

        IScrollGridWidget grid = builder.addScrollGridWidget(outputs, 8, 5);
        grid.setPosition(0, 20);
    }

    @Override
    public void draw(ModifierCompatibilityRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        var visualOrder = recipe.modifier().getDisplayName().getVisualOrderText();
        int textWidth = font.width(visualOrder);
        guiGraphics.text(font, visualOrder, 81 - textWidth / 2, 5, 0xFF505050, false);
    }
}