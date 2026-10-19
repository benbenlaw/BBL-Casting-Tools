package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.network.packet.ConfiguratorKeybindPacket;
import com.benbenlaw.castingtools.network.packet.ConfiguratorLevelPacket;
import com.benbenlaw.castingtools.utils.KeyCombo;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ConfiguratorScreen extends AbstractContainerScreen<ConfiguratorMenu> {

    private static final Identifier TEXTURE = CastingTools.identifier("textures/gui/configurator_gui.png");
    private static final Identifier SCROLL_ICON = CastingTools.identifier("scroll");
    private static final Identifier WRENCH_ICON = CastingTools.identifier("wrench");

    private static final int LIST_X = 29;
    private static final int LIST_Y = 17;
    private static final int LIST_WIDTH = 121;
    private static final int ROW_HEIGHT = 17;
    private static final int ROW_SPACING = 17;
    private static final int VISIBLE_ROWS = 3;

    private static final int WRENCH_SIZE = 17;
    private static final int SLIDER_WIDTH = LIST_WIDTH - WRENCH_SIZE - 1;

    private static final int SCROLLBAR_X = 154;
    private static final int SCROLLBAR_Y = 17;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 52;
    private static final int SCROLLER_HEIGHT = 15;

    private record Entry(Identifier id, Component name, int installed, int active, int keybind) {}

    private int scroll = 0;
    private boolean draggingScrollbar = false;
    private String signature = "";
    private Identifier settingsId = null;
    private boolean listeningForKey = false;
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
            result.add(new Entry(id, Component.translatable(modifier.getData().displayName()), installed,
                    comp.activeLevel(id), comp.keybind(id)));
        });
        result.sort(Comparator.comparing(e -> e.name().getString()));
        return result;
    }

    private Entry settingsEntry(List<Entry> entries) {
        if (settingsId == null) return null;
        for (Entry entry : entries) {
            if (entry.id().equals(settingsId)) return entry;
        }
        return null;
    }

    private String signatureOf(List<Entry> entries) {
        StringBuilder builder = new StringBuilder();
        builder.append(settingsId).append('|').append(listeningForKey).append('|');
        for (Entry entry : entries) {
            builder.append(entry.id()).append(':').append(entry.installed()).append(':').append(entry.keybind()).append(';');
        }
        return builder.toString();
    }

    private static int rowGap(int row) {
        if (row == 0) return -1;
        if (row == VISIBLE_ROWS - 1) return 1;
        return 0;
    }

    private int rowY(int row) {
        return topPos + LIST_Y + row * ROW_SPACING + rowGap(row);
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

        Entry settings = settingsEntry(entries);
        if (settingsId != null && settings == null) {
            // The tool was taken out or changed, fall back to the list
            settingsId = null;
        }
        signature = signatureOf(entries);

        if (settings != null) {
            buildSettings(settings);
            return;
        }

        scroll = Math.max(0, Math.min(scroll, maxScroll(entries.size())));

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int index = scroll + row;
            if (index >= entries.size()) break;
            Entry entry = entries.get(index);
            int y = rowY(row);

            ModifierSlider slider = new ModifierSlider(leftPos + LIST_X, y, entry);
            sliders.add(slider);
            this.addRenderableWidget(slider);

            SpriteIconButton wrench = SpriteIconButton.builder(Component.translatable("tooltip.castingtools.configurator.settings"),
                    button -> openSettings(entry.id()), true)
                    .width(WRENCH_SIZE)
                    .sprite(WRENCH_ICON, 11, 11)
                    .build();
            wrench.setPosition(leftPos + LIST_X + SLIDER_WIDTH + 1, y);
            wrench.setHeight(ROW_HEIGHT);
            this.addRenderableWidget(wrench);
        }
    }

    private void buildSettings(Entry entry) {
        Button keyButton = Button.builder(keyButtonLabel(entry), button -> {
                    listeningForKey = true;
                    rebuild();
                })
                .bounds(leftPos + LIST_X, rowY(1), LIST_WIDTH, ROW_HEIGHT)
                .build();
        this.addRenderableWidget(keyButton);

        Button back = Button.builder(Component.translatable("tooltip.castingtools.configurator.back"), button -> {
                    settingsId = null;
                    listeningForKey = false;
                    rebuild();
                })
                .bounds(leftPos + LIST_X, rowY(2), LIST_WIDTH, ROW_HEIGHT)
                .build();
        this.addRenderableWidget(back);
    }

    private Component keyButtonLabel(Entry entry) {
        Component value;
        if (listeningForKey) {
            return Component.translatable("tooltip.castingtools.configurator.press_key");
        } else if (entry.keybind() == 0) {
            value = Component.translatable("tooltip.castingtools.configurator.none");
        } else {
            value = keyName(entry.keybind());
        }
        return Component.translatable("tooltip.castingtools.configurator.toggle_key").append(": ").append(value);
    }

    private static Component keyName(int combo) {
        MutableComponent name = Component.empty();
        int modifiers = KeyCombo.modifiers(combo);
        if ((modifiers & KeyCombo.MOD_CONTROL) != 0) name.append(Component.translatable("tooltip.castingtools.configurator.mod_ctrl")).append(" + ");
        if ((modifiers & KeyCombo.MOD_ALT) != 0) name.append(Component.translatable("tooltip.castingtools.configurator.mod_alt")).append(" + ");
        if ((modifiers & KeyCombo.MOD_SHIFT) != 0) name.append(Component.translatable("tooltip.castingtools.configurator.mod_shift")).append(" + ");
        return name.append(InputConstants.Type.KEYSYM.getOrCreate(KeyCombo.key(combo)).getDisplayName());
    }

    private void openSettings(Identifier id) {
        settingsId = id;
        listeningForKey = false;
        rebuild();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        List<Entry> entries = entries();
        Entry settings = settingsEntry(entries);

        if (settings != null) {
            renderSettingsHeader(guiGraphics, settings);
            return;
        }

        if (entries.isEmpty()) {
            Component hint = Component.translatable("tooltip.castingtools.configurator.insert");
            guiGraphics.text(font, hint, leftPos + LIST_X + (LIST_WIDTH - font.width(hint)) / 2, topPos + LIST_Y + 22, 0xFF555555, false);
        }

        for (ModifierSlider slider : sliders) {
            guiGraphics.text(font, fitted(slider.label(), SLIDER_WIDTH - 10), slider.getX() + 4, slider.getY() + (ROW_HEIGHT - 8) / 2 + 1, 0xFFFFFFFF, true);
        }

        renderScrollbar(guiGraphics, entries.size());
    }

    private void renderSettingsHeader(GuiGraphicsExtractor guiGraphics, Entry entry) {
        int y = rowY(0) + (ROW_HEIGHT - 8) / 2 + 1;
        guiGraphics.text(font, fitted(entry.name(), LIST_WIDTH - 4), leftPos + LIST_X + 2, y, 0xFF404040, false);
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
    public boolean keyPressed(KeyEvent event) {
        if (listeningForKey && settingsId != null) {
            if (KeyCombo.isModifierKey(event.key())) {
                // Shift, Ctrl and Alt are held down for a combo, keep waiting for the actual key
                return true;
            }
            // Escape clears the binding, same as the Controls screen
            int combo = event.key() == InputConstants.KEY_ESCAPE ? 0 : KeyCombo.encode(event.key(), event.modifiers());
            ClientPacketDistributor.sendToServer(new ConfiguratorKeybindPacket(settingsId, combo));
            listeningForKey = false;
            rebuild();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (listeningForKey) {
            listeningForKey = false;
            rebuild();
        }
        if (settingsId == null && event.button() == 0 && overScrollbar(event.x(), event.y()) && maxScroll(entries().size()) > 0) {
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
        if (settingsId == null
                && mouseX >= leftPos + LIST_X && mouseX < leftPos + SCROLLBAR_X + SCROLLBAR_WIDTH
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
            super(x, y, SLIDER_WIDTH, ROW_HEIGHT, Component.empty(), entry.active() / (double) entry.installed());
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
