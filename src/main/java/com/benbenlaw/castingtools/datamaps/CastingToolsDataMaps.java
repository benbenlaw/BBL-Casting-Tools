package com.benbenlaw.castingtools.datamaps;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class CastingToolsDataMaps {

    public static final DataMapType<EntityType<?>, ItemStackTemplate> BEHEADING_DROPS = DataMapType.builder(
            CastingTools.identifier("beheading_drops"), Registries.ENTITY_TYPE, ItemStackTemplate.CODEC).synced(ItemStackTemplate.CODEC, true).build();

    public static final DataMapType<Block, Identifier> PULVERIZING_BLOCKS = DataMapType.builder(
            CastingTools.identifier("pulverizing_blocks"), Registries.BLOCK, Identifier.CODEC).synced(Identifier.CODEC, true).build();

}
