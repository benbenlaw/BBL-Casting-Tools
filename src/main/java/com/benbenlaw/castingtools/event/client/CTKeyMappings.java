package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.network.packet.ModifierToggleKeyPacket;
import com.benbenlaw.castingtools.utils.KeyCombo;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * Keys picked in the wrench menu of the Configurator. They are stored on the tool itself, so rather than
 * vanilla key mappings we watch raw key presses and tell the server when one belongs to a held or worn modifier.
 */
@EventBusSubscriber(modid = CastingTools.MOD_ID, value = Dist.CLIENT)
public class CTKeyMappings {

    private static final int GLFW_PRESS = 1;

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null) return;
        if (event.getAction() != GLFW_PRESS) return;

        if (event.getKey() <= 0 || KeyCombo.isModifierKey(event.getKey())) return;

        int combo = KeyCombo.encode(event.getKey(), event.getModifiers());
        if (!hasBoundModifier(minecraft.player, combo)) return;

        ClientPacketDistributor.sendToServer(new ModifierToggleKeyPacket(combo));
    }

    private static boolean hasBoundModifier(Player player, int key) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT);
            if (comp != null && comp.keybinds().containsValue(key)) return true;
        }
        return false;
    }
}
