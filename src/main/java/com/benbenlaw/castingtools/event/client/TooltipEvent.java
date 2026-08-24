package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.CTTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Objects;

@EventBusSubscriber(modid = CastingTools.MOD_ID, value = Dist.CLIENT)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT.get());

        if (comp != null && !comp.modifiers().isEmpty()) {

            if (Minecraft.getInstance().hasShiftDown()) {

                boolean isEnhanced = stack.is(CTTags.Items.ENHANCED);

                if (isEnhanced) {
                    event.getToolTip().add(rainbowText("Enhanced"));
                }
                event.getToolTip().add(Component.literal("Modifiers:").withStyle(ChatFormatting.BLUE));


                comp.modifiers().forEach((location, level) -> {
                    ModifierData modifier = Objects.requireNonNull(ModifierRegistry.MODIFIER_REGISTRY.getValue(location)).getData();

                    if (modifier != null) {
                        Component name = Component.translatable(modifier.displayName());

                        event.getToolTip().add(Component.literal(" - ")
                                .append(name.copy().withStyle(ChatFormatting.GOLD))
                                .append(Component.literal(" " + level).withStyle(ChatFormatting.YELLOW)));
                    }
                });
            } else {
                event.getToolTip().add(Component.translatable("tooltip.bblcore.shift")
                        .withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    private static final ChatFormatting[] RAINBOW = new ChatFormatting[] {
            ChatFormatting.RED,
            ChatFormatting.GOLD,
            ChatFormatting.YELLOW,
            ChatFormatting.GREEN,
            ChatFormatting.AQUA,
            ChatFormatting.BLUE,
            ChatFormatting.LIGHT_PURPLE
    };

    private static Component rainbowText(String text) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (char c : text.toCharArray()) {
            ChatFormatting color = RAINBOW[i % RAINBOW.length];
            result.append(color).append(c);
            i++;
        }

        return Component.literal(result.toString());
    }
}
