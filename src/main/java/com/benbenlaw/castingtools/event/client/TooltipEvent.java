package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT.get());

        if (comp != null && !comp.modifiers().isEmpty()) {

            if (Minecraft.getInstance().hasShiftDown()) {
                event.getToolTip().add(Component.literal("Modifiers:").withStyle(ChatFormatting.BLUE));

                comp.modifiers().forEach((location, level) -> {
                    Modifier modifier = ModifierRegistry.REGISTRY.getValue(location);

                    if (modifier != null) {
                        Component name = modifier.getDisplayName();

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
}
