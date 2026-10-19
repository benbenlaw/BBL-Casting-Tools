package com.benbenlaw.castingtools.network.packet;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Client to server: a key (or key combo) bound to modifiers was pressed. Switches every modifier bound to that
 * key on held and worn items off (limit 0), or back on at the level the user had set, and reports the result
 * in a single action bar message.
 */
public record ModifierToggleKeyPacket(int key) implements CustomPacketPayload {

    public static final Type<ModifierToggleKeyPacket> TYPE = new Type<>(CastingTools.identifier("modifier_toggle_key"));

    public static final IPayloadHandler<ModifierToggleKeyPacket> HANDLER = (pkt, ctx) -> {
        if (!(ctx.player() instanceof ServerPlayer player)) return;
        if (pkt.key() <= 0) return;

        Set<Identifier> switchedOff = new LinkedHashSet<>();
        Set<Identifier> switchedOn = new LinkedHashSet<>();

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(equipmentSlot);
            ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT);
            if (comp == null) continue;

            List<Identifier> bound = new ArrayList<>();
            comp.keybinds().forEach((id, key) -> {
                if (key == pkt.key() && comp.modifiers().containsKey(id)) bound.add(id);
            });
            if (bound.isEmpty()) continue;

            ModifierComponent updated = comp;
            for (Identifier id : bound) {
                if (updated.activeLevel(id) > 0) {
                    switchedOff.add(id);
                } else {
                    switchedOn.add(id);
                }
                updated = updated.toggled(id);
            }
            stack.set(CTDataComponent.MODIFIER_COMPONENT, updated);
        }

        MutableComponent message = Component.empty();
        appendGroup(message, switchedOff, "tooltip.castingtools.configurator.toggled_off", ChatFormatting.RED);
        appendGroup(message, switchedOn, "tooltip.castingtools.configurator.toggled_on", ChatFormatting.GREEN);
        if (!message.getSiblings().isEmpty()) {
            player.sendSystemMessage(message, true);
        }
    };

    /** Adds "Name, Name: State" to the message, groups are separated by a gap. */
    private static void appendGroup(MutableComponent message, Set<Identifier> ids, String stateKey, ChatFormatting color) {
        if (ids.isEmpty()) return;

        MutableComponent group = Component.empty();
        boolean first = true;
        for (Identifier id : ids) {
            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
            if (modifier == null) continue;
            if (!first) group.append(", ");
            group.append(Component.translatable(modifier.getData().displayName()));
            first = false;
        }
        if (first) return;

        group.append(": ").append(Component.translatable(stateKey));
        group.withStyle(color);

        if (!message.getSiblings().isEmpty()) message.append("   ");
        message.append(group);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierToggleKeyPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ModifierToggleKeyPacket::key,
            ModifierToggleKeyPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
