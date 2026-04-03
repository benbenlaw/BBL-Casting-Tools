package com.benbenlaw.castingtools.datamaps;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class CastingToolsDataMaps {

    public static final DataMapType<EntityType<?>, ItemStackTemplate> BEHEADING_DROPS = DataMapType.builder(
            CastingTools.identifier("beheading_drops"), Registries.ENTITY_TYPE, ItemStackTemplate.CODEC).synced(ItemStackTemplate.CODEC, true).build();


}
