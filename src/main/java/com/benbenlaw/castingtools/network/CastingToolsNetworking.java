package com.benbenlaw.castingtools.network;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.network.packet.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CastingToolsNetworking {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar(CastingTools.MOD_ID);

        //Server to Client
        registrar.playToClient(ModifiersSyncPacket.TYPE, ModifiersSyncPacket.STREAM_CODEC, ModifiersSyncPacket.HANDLER);

        //Client to Server
        registrar.playToServer(JetJumpPacket.TYPE, JetJumpPacket.STREAM_CODEC, JetJumpPacket.HANDLER);
        registrar.playToServer(ConfiguratorLevelPacket.TYPE, ConfiguratorLevelPacket.STREAM_CODEC, ConfiguratorLevelPacket.HANDLER);
        registrar.playToServer(ConfiguratorKeybindPacket.TYPE, ConfiguratorKeybindPacket.STREAM_CODEC, ConfiguratorKeybindPacket.HANDLER);
        registrar.playToServer(ModifierToggleKeyPacket.TYPE, ModifierToggleKeyPacket.STREAM_CODEC, ModifierToggleKeyPacket.HANDLER);
    }
}
