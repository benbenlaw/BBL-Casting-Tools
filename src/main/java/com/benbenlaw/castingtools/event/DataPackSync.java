package com.benbenlaw.castingtools.event;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.datamaps.CTDataMaps;
import com.benbenlaw.castingtools.event.client.ClientRecipeCache;
import com.benbenlaw.castingtools.intergration.custom.BeheadingRecipe;
import com.benbenlaw.castingtools.intergration.custom.PulverizingRecipe;
import com.benbenlaw.castingtools.intergration.custom.TreasureRecipe;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.network.packet.ModifiersSyncPacket;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import java.util.HashMap;
import java.util.List;
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

    @SubscribeEvent
    public static void onDataMapSync(DataMapsUpdatedEvent event) {

        // Sync Beheading recipes
        event.ifRegistry(Registries.ENTITY_TYPE, registry -> {
            Map<Identifier, BeheadingRecipe> beheadingRecipeHashMap = new HashMap<>();

            registry.listElementIds().forEach(entityKey -> {
                Holder<EntityType<?>> entityType = registry.getOrThrow(entityKey);
                ItemStackTemplate head = entityType.getData(CTDataMaps.BEHEADING_DROPS);

                if (head != null) {
                    beheadingRecipeHashMap.put(entityKey.identifier(), new BeheadingRecipe(entityType.value(), head));
                }
            });
            ClientRecipeCache.setCachedBeheadingRecipes(beheadingRecipeHashMap);
        });

        // Sync Pulverizing and Treasure recipes
        event.ifRegistry(Registries.BLOCK, registry -> {
            Map<Identifier, PulverizingRecipe> pulverizingRecipeHashMap = new HashMap<>();
            Map<Identifier, TreasureRecipe> treasureRecipeHashMap = new HashMap<>();

            registry.listElementIds().forEach(blockKey -> {
                Holder<Block> blockHolder = registry.getOrThrow(blockKey);

                List<ChanceResult> pulverizing = blockHolder.getData(CTDataMaps.PULVERIZING_BLOCKS);
                if (pulverizing != null) {
                    pulverizingRecipeHashMap.put(blockKey.identifier(),
                            new PulverizingRecipe(blockHolder.value(), pulverizing));
                }

                List<ChanceResult> treasure = blockHolder.getData(CTDataMaps.TREASURE);
                if (treasure != null) {
                    treasureRecipeHashMap.put(blockKey.identifier(),
                            new TreasureRecipe(blockHolder.value(), treasure));
                }
            });

            ClientRecipeCache.setCachedPulverizingRecipes(pulverizingRecipeHashMap);
            ClientRecipeCache.setCachedTreasureRecipes(treasureRecipeHashMap);
        });
    }
}
