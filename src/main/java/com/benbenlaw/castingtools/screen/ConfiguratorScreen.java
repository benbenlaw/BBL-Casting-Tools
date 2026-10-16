package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.network.packet.ConfiguratorLevelPacket;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ConfiguratorScreen extends AbstractContainerScreen<ConfiguratorMenu> {

    private static final Identifier TEXTURE = CastingTools.identifier("textures/gui/configurator_gui.png");
    private static final Identifier SCROLL_ICON = CastingTools.identifier("scroll");

    private static final int LIST_X = 29;
    private static final int LIST_Y = 17;
    private static final int LIST_WIDTH = 121;
    private static final int ROW_HEIGHT = 17;
    private static final int ROW_SPACING = 17;
    private static final int VISIBLE_ROWS = 3;

    private static final int SCROLLBAR_X = 154;
    private static final int SCROLLBAR_Y = 17;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 52;
    private static final int SCROLLER_HEIGHT = 15;

    private record Entry(Identifier id, Component name, int installed, int active) {}

    private int scroll = 0;
    private boolean draggingScrollbar = false;
    private String signature = "";
    private final List<ModifierSlider> sliders = new ArrayList<>();

    public ConfiguratorScreen(ConfiguratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    private List<Entry> entries() {
        List<Entry> result = new ArrayList<>();
        ModifierComponent comp = menu.getToolStack().get(CTDataComponent.MODIFIER_COMPONENT);
        if (comp == null) return result;

        comp.modifiers().forEach((id, installed) -> {
            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
            if (modifier == null || installed <= 0) return;
            result.add(new Entry(id, Component.translatable(modifier.getData().displayName()), installed, comp.activeLevel(id)));
        });
        result.sort(Comparator.comparing(e -> e.name().getString()));
        return result;
    }

    private static String signatureOf(List<Entry> entries) {
        StringBuilder builder = new StringBuilder();
        for (Entry entry : entries) {
            builder.append(entry.id()).append(':').append(entry.installed()).append(';');
        }
        return builder.toString();
    }

    private static int rowGap(int row) {
        if (row == 0) return -1;
        if (row == VISIBLE_ROWS - 1) return 1;
        return 0;
    }

    private int maxScroll(int entryCount) {
        return Math.max(0, entryCount - VISIBLE_ROWS);
    }

    @Override
    protected void init() {
        super.init();
        rebuild();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (!signatureOf(entries()).equals(signature)) {
            rebuild();
        }
    }

    private void rebuild() {
        this.clearWidgets();
        sliders.clear();

        List<Entry> entries = entries();
        signature = signatureOf(entries);
        scroll = Math.max(0, Math.min(scroll, maxScroll(entries.size())));

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int index = scroll + row;
            if (index >= entries.size()) break;
            ModifierSlider slider = new ModifierSlider(leftPos + LIST_X, topPos + LIST_Y + row * ROW_SPACING + rowGap(row), entries.get(index));
            sliders.add(slider);
            this.addRenderableWidget(slider);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int count = entries().size();
        if (count == 0) {
            Component hint = Component.translatable("tooltip.castingtools.configurator.insert");
            guiGraphics.text(font, hint, leftPos + LIST_X + (LIST_WIDTH - font.width(hint)) / 2, topPos + LIST_Y + 22, 0xFF555555, false);
        }

        for (ModifierSlider slider : sliders) {
            guiGraphics.text(font, fitted(slider.label(), LIST_WIDTH - 10), slider.getX() + 4, slider.getY() + (ROW_HEIGHT - 8) / 2 + 1, 0xFFFFFFFF, true);
        }

        renderScrollbar(guiGraphics, count);
    }

    private Component fitted(Component label, int maxWidth) {
        String text = label.getString();
        if (font.width(text) <= maxWidth) return label;
        while (text.length() > 1 && font.width(text + "...") > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }
        return Component.literal(text + "...");
    }

    private void renderScrollbar(GuiGraphicsExtractor guiGraphics, int entryCount) {
        int max = maxScroll(entryCount);
        if (max == 0) return;

        int thumbY = topPos + SCROLLBAR_Y + (SCROLLBAR_HEIGHT - SCROLLER_HEIGHT) * scroll / max;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLL_ICON, leftPos + SCROLLBAR_X, thumbY, SCROLLBAR_WIDTH, SCROLLER_HEIGHT);
    }

    private boolean overScrollbar(double mouseX, double mouseY) {
        return mouseX >= leftPos + SCROLLBAR_X && mouseX < leftPos + SCROLLBAR_X + SCROLLBAR_WIDTH
                && mouseY >= topPos + SCROLLBAR_Y && mouseY < topPos + SCROLLBAR_Y + SCROLLBAR_HEIGHT;
    }

    private void scrollTo(double mouseY) {
        int max = maxScroll(entries().size());
        if (max == 0) return;

        double travel = SCROLLBAR_HEIGHT - SCROLLER_HEIGHT;
        double fraction = (mouseY - topPos - SCROLLBAR_Y - SCROLLER_HEIGHT / 2.0) / travel;
        int newScroll = Math.max(0, Math.min(max, (int) Math.round(fraction * max)));
        if (newScroll != scroll) {
            scroll = newScroll;
            rebuild();
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && overScrollbar(event.x(), event.y()) && maxScroll(entries().size()) > 0) {
            draggingScrollbar = true;
            scrollTo(event.y());
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (draggingScrollbar) {
            scrollTo(event.y());
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int count = entries().size();
        if (mouseX >= leftPos + LIST_X && mouseX < leftPos + SCROLLBAR_X + SCROLLBAR_WIDTH
                && mouseY >= topPos + LIST_Y && mouseY < topPos + LIST_Y + VISIBLE_ROWS * ROW_SPACING) {
            int newScroll = Math.max(0, Math.min(maxScroll(count), scroll - (int) Math.signum(scrollY)));
            if (newScroll != scroll) {
                scroll = newScroll;
                rebuild();
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private static class ModifierSlider extends AbstractSliderButton {

        private final Entry entry;
        private int level;
        private Component label = Component.empty();

        ModifierSlider(int x, int y, Entry entry) {
            super(x, y, LIST_WIDTH, ROW_HEIGHT, Component.empty(), entry.active() / (double) entry.installed());
            this.entry = entry;
            this.level = entry.active();
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int shown = (int) Math.round(this.value * entry.installed());
            Component state = shown <= 0
                    ? Component.translatable("tooltip.castingtools.configurator.off")
                    : Component.literal(shown + "/" + entry.installed());
            label = Component.empty().append(entry.name()).append(": ").append(state);
        }

        Component label() {
            return label;
        }

        @Override
        protected void applyValue() {
            int newLevel = (int) Math.round(this.value * entry.installed());
            this.value = newLevel / (double) entry.installed();
            if (newLevel != level) {
                level = newLevel;
                ClientPacketDistributor.sendToServer(new ConfiguratorLevelPacket(entry.id(), newLevel));
            }
        }
    }
}
