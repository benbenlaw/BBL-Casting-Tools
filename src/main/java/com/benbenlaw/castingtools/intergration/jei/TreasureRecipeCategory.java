package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.intergration.custom.TreasureRecipe;
import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.core.util.MouseUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TreasureRecipeCategory implements IRecipeCategory<TreasureRecipe> {

    public static final Identifier TEXTURE = CastingTools.identifier("textures/gui/data_map_jei.png");
    public static final IRecipeType<TreasureRecipe> RECIPE_TYPE = IRecipeType.create(CastingTools.identifier("treasure"), TreasureRecipe.class);

    private final int width = 101;
    private final int height = 20;
    private final IDrawable icon;

    public TreasureRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.EMERALD));
    }

    @Override
    public @NotNull IRecipeType<TreasureRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.castingtools.treasure");
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
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TreasureRecipe recipe, IFocusGroup focuses) {
        int centerX = 48;
        int centerY = 2;
        int slotWidth = 18;

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 2).add(new ItemStack(recipe.block())).setBackground(JEICastingToolsPlugin.slotDrawable, -1, -1);

        List<ChanceResult> chanceResults = recipe.results();
        int totalResults = chanceResults.size();

        for (int i = 0; i < totalResults; i++) {
            int displayIndex = Math.min(i, 2);
            int xPos = centerX + (displayIndex * slotWidth);

            final int finalIndex = i;

            builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, centerY)
                    .add(chanceResults.get(i).template().create()).addRichTooltipCallback((slotView, tooltip) -> {
                        ChanceResult output = chanceResults.get(finalIndex);
                        float chance = output.chance();
                        int displayChance = (int) (chance * 100);
                        tooltip.add(Component.translatable("jei.castingtools.chance", displayChance).withStyle(ChatFormatting.GOLD));
                    }).setBackground(JEICastingToolsPlugin.slotDrawable, -1, -1);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, TreasureRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> results = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT);

        if (results.size() > 3) {
            IScrollGridWidget triggersGrid = builder.addScrollGridWidget(results, 2, 1);
            triggersGrid.setPosition(47, 1);
        }
        builder.addAnimatedRecipeArrow(200).setPosition(21, 2);
    }


    public void draw(TreasureRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, TreasureRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 21, 2, 0, 0, 28, 18)) {
            tooltip.add(Component.translatable("tooltip.castingtools.treasure", recipe.block().getName()));
        }
    }
}
