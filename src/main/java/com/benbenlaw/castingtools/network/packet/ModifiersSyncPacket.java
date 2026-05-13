package com.benbenlaw.castingtools.network.packet;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.HashMap;
import java.util.Map;

public record ModifiersSyncPacket(Map<Identifier, ModifierData> data) implements CustomPacketPayload {

    public static final Type<ModifiersSyncPacket> TYPE = new Type<>(CastingTools.identifier("sync_modifiers"));

    public static final IPayloadHandler<ModifiersSyncPacket> HANDLER = (packet, context) -> {
        for (var entry : packet.data.entrySet()) {
            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(entry.getKey());
            if (modifier != null) {
                modifier.setData(entry.getValue());
            }
        }
    };


    public static final StreamCodec<RegistryFriendlyByteBuf, ModifiersSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ModifierData.STREAM_CODEC),
            ModifiersSyncPacket::data,
            ModifiersSyncPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}