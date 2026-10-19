package com.benbenlaw.castingtools.network.packet;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.screen.ConfiguratorMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

/** Client to server: bind a modifier on the tool in the open Configurator to a keyboard key code (0 = none). */
public record ConfiguratorKeybindPacket(Identifier modifier, int key) implements CustomPacketPayload {

    public static final Type<ConfiguratorKeybindPacket> TYPE = new Type<>(CastingTools.identifier("configurator_keybind"));

    public static final IPayloadHandler<ConfiguratorKeybindPacket> HANDLER = (pkt, ctx) -> {
        if (ctx.player() instanceof ServerPlayer player && player.containerMenu instanceof ConfiguratorMenu menu) {
            menu.setKeybind(pkt.modifier(), pkt.key());
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, ConfiguratorKeybindPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ConfiguratorKeybindPacket::modifier,
            ByteBufCodecs.VAR_INT, ConfiguratorKeybindPacket::key,
            ConfiguratorKeybindPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
