package com.benbenlaw.castingtools.screen;

import com.benbenlaw.casting.Casting;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import com.benbenlaw.core.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.List;

public class ModifierScreen extends AbstractContainerScreen<ModifierMenu> {

    private static final Identifier TEXTURE = CastingTools.identifier("textures/gui/modifier_gui.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    public ModifierScreen(ModifierMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderTankTextures(guiGraphics, x, y);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderTankTooltips(guiGraphics, x, y, mouseX, mouseY);
    }

    private void renderTankTextures(GuiGraphicsExtractor guiGraphics, int x, int y) {
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 20, 16, 47);
        drawTankFluid(guiGraphics, menu.blockEntity.getInputFluidHandler(), 1, x + 35, y + 20, 16, 47);
    }


    private void drawTankFluid(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height) {
        var fluidHandler = (FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

        if (!stack.isEmpty()) {
            int capacity = fluidHandler.getCapacityAsInt(slot, FluidResource.of(stack));
            int displayLevel = (int) ((float) stack.getAmount() / (float) capacity * (float) height);
            FluidRenderingUtils.renderFluidStack(guiGraphics, stack, x, y + height - displayLevel, width, displayLevel, 0, 0);
        }
    }

    private void renderTankTooltips(GuiGraphicsExtractor guiGraphics, int x, int y, int mouseX, int mouseY) {
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 0, x + 8, y + 20, 16, 47, mouseX, mouseY, "Empty");
        drawTankTooltip(guiGraphics, menu.blockEntity.getInputFluidHandler(), 1, x + 35, y + 20,16, 47, mouseX, mouseY, "Empty");


    }

    private void drawTankTooltip(GuiGraphicsExtractor guiGraphics, Object handler, int slot, int x, int y, int width, int height, int mouseX, int mouseY, String emptyName) {
        var fluidHandler = (FluidStacksResourceHandler) handler;
        var stack = FluidUtil.getStack(fluidHandler, slot);

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            if (stack.isEmpty()) {
                Component text = Component.literal(emptyName);
                List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
                guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            } else {
                FluidRenderingUtils.renderFluidStackTooltip(guiGraphics, stack, fluidHandler, slot, x, y, width, height, mouseX, mouseY);
            }
        }
    }
}