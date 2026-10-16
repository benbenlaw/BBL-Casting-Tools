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

public record ConfiguratorLevelPacket(Identifier modifier, int level) implements CustomPacketPayload {

    public static final Type<ConfiguratorLevelPacket> TYPE = new Type<>(CastingTools.identifier("configurator_level"));

    public static final IPayloadHandler<ConfiguratorLevelPacket> HANDLER = (pkt, ctx) -> {
        if (ctx.player() instanceof ServerPlayer player && player.containerMenu instanceof ConfiguratorMenu menu) {
            menu.setActiveLevel(pkt.modifier(), pkt.level());
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, ConfiguratorLevelPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ConfiguratorLevelPacket::modifier,
            ByteBufCodecs.VAR_INT, ConfiguratorLevelPacket::level,
            ConfiguratorLevelPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
