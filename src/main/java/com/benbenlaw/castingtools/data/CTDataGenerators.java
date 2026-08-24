package com.benbenlaw.castingtools.data;


import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.data.custom.ModifierProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class CTDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new CTBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new CTModelProvider(packOutput));

        generator.addProvider(true, new CTItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new CTFluidTags(packOutput, lookupProvider));
        generator.addProvider(true, new CTLangProvider(packOutput));
        generator.addProvider(true, new CTEquipmentAssetProvider(packOutput));
        generator.addProvider(true, new CTEquipmentAsset(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(CTLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //Recipes
        generator.addProvider(true, new CTRecipeProvider.Runner(packOutput, lookupProvider));

        //Custom
        generator.addProvider(true, new ModifierProvider(packOutput, lookupProvider));

        //Data Maps
        generator.addProvider(true, new CTDataMapProvider(packOutput, lookupProvider));
    }
}
