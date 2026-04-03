package com.benbenlaw.castingtools.event;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.network.packet.ModifiersSyncPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class DataPackSync {

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        Map<Identifier, ModifierData> currentData = new HashMap<>();

        ModifierRegistry.MODIFIER_REGISTRY.entrySet().forEach(entry -> {
            Modifier modifier = entry.getValue();
            if (modifier.getData() != null) {
                currentData.put(entry.getKey().identifier(), modifier.getData());
            }
        });

        ModifiersSyncPacket packet = new ModifiersSyncPacket(currentData);

        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(), packet);
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        }
    }
}
