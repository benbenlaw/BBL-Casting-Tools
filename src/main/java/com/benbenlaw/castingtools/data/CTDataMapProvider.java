package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.datamaps.CastingToolsDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class CTDataMapProvider extends DataMapProvider {

    public CTDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(CastingToolsDataMaps.BEHEADING_DROPS)
                .add(EntityType.SKELETON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.SKELETON_SKULL), false)
                .add(EntityType.CREEPER.builtInRegistryHolder().key(), new ItemStackTemplate(Items.CREEPER_HEAD), false)
                .add(EntityType.PIGLIN.builtInRegistryHolder().key(), new ItemStackTemplate(Items.PIGLIN_HEAD), false)
                .add(EntityType.WITHER_SKELETON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.WITHER_SKELETON_SKULL), false)
                .add(EntityType.ZOMBIE.builtInRegistryHolder().key(), new ItemStackTemplate(Items.ZOMBIE_HEAD), false)
                .add(EntityType.PLAYER.builtInRegistryHolder().key(), new ItemStackTemplate(Items.PLAYER_HEAD), false)
                .add(EntityType.ENDER_DRAGON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.DRAGON_HEAD), false)
        ;

        builder(CastingToolsDataMaps.PULVERIZING_BLOCKS)
                .add(Blocks.COBBLESTONE.defaultBlockState().typeHolder(), Identifier.withDefaultNamespace("blocks/gravel"), false)
                .add(Blocks.GRAVEL.defaultBlockState().typeHolder(), Identifier.withDefaultNamespace("blocks/sand"), false)
                .add(Blocks.STONE_BRICKS.defaultBlockState().typeHolder(), Identifier.withDefaultNamespace("blocks/cracked_stone_bricks"), false)


        ;
    }
}
