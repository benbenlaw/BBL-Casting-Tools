package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class UpgraderScreen extends AbstractContainerScreen<UpgraderMenu> {

    private static final Identifier TEXTURE = CastingTools.identifier("textures/gui/upgrader_gui.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    public UpgraderScreen(UpgraderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        FluidRenderingUtils.renderFluid(guiGraphics, menu.blockEntity.getFluidHandler(), 0, x, y,
                8, 20, 47, 16, mouseX, mouseY, Component.translatable("tooltip.casting.empty"));

        FluidRenderingUtils.renderFluid(guiGraphics, menu.blockEntity.getFluidHandler(), 1, x, y,
                35, 20, 47, 16, mouseX, mouseY, Component.translatable("tooltip.casting.empty"));
    }
}