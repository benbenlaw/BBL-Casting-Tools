package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.modifier.Modifier;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ModifierIngredientRenderer implements IIngredientRenderer<Modifier> {

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, Modifier ingredient) {
        Font font = Minecraft.getInstance().font;
        String label = ingredient.getDisplayName().getString();
        String shortLabel = label.length() > 2 ? label.substring(0, 2) : label;
        int textWidth = font.width(shortLabel);
        guiGraphics.text(font, shortLabel, (16 - textWidth) / 2, 4, 0xFFFFFFFF, false);



    }

    @Override
    public List<Component> getTooltip(Modifier ingredient, TooltipFlag tooltipFlag) {
        return List.of(
                Component.translatable("modifier.castingtools.modifier", ingredient.getDisplayName(),
                ingredient.getDescription()),
                Component.translatable("modifier.castingtools.max_level", ingredient.getMaxLevel()).withStyle(ChatFormatting.AQUA),
                Component.translatable("modifier.castingtools.max_enhanced_level", ingredient.getMaxEnhancedLevel()).withStyle(ChatFormatting.AQUA));
    }
}