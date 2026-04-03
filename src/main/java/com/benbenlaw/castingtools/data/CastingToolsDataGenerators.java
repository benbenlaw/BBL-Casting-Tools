package com.benbenlaw.castingtools.data;


import com.benbenlaw.casting.data.CastingModelProvider;
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
import java.util.concurrent.ExecutionException;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class CastingToolsDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new CastingToolsBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new CastingToolsModelProvider(packOutput));

        generator.addProvider(true, new CastingToolsItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new CastingToolsLangProvider(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(CastingToolsLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //Recipes
        generator.addProvider(true, new CastingToolsRecipeProvider.Runner(packOutput, lookupProvider));

        //Custom
        generator.addProvider(true, new ModifierProvider(packOutput, lookupProvider));

        //Data Maps
        generator.addProvider(true, new BeheadingDropsDataMapProvider(packOutput, lookupProvider));
    }
}
