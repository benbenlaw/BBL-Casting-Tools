package com.benbenlaw.castingtools.datamaps;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.List;

public class CTDataMaps {


    public static final DataMapType<EntityType<?>, ItemStackTemplate> BEHEADING_DROPS = DataMapType.builder(
            CastingTools.identifier("beheading_drops"), Registries.ENTITY_TYPE, ItemStackTemplate.CODEC).synced(ItemStackTemplate.CODEC, true).build();

    public static final DataMapType<Block, List<ChanceResult>> PULVERIZING_BLOCKS = DataMapType.builder(
            CastingTools.identifier("pulverizing_blocks"), Registries.BLOCK, ChanceResult.CODEC.listOf()).synced(ChanceResult.CODEC.listOf(), true).build();

    public static final DataMapType<Block, List<ChanceResult>> TREASURE = DataMapType.builder(
            CastingTools.identifier("treasure"), Registries.BLOCK, ChanceResult.CODEC.listOf()).synced(ChanceResult.CODEC.listOf(), true).build();

}


