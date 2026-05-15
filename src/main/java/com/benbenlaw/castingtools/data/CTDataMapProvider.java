package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.datamaps.CTDataMaps;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class CTDataMapProvider extends DataMapProvider {

    public CTDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(CTDataMaps.BEHEADING_DROPS)
                .add(EntityType.SKELETON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.SKELETON_SKULL), false)
                .add(EntityType.CREEPER.builtInRegistryHolder().key(), new ItemStackTemplate(Items.CREEPER_HEAD), false)
                .add(EntityType.PIGLIN.builtInRegistryHolder().key(), new ItemStackTemplate(Items.PIGLIN_HEAD), false)
                .add(EntityType.WITHER_SKELETON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.WITHER_SKELETON_SKULL), false)
                .add(EntityType.ZOMBIE.builtInRegistryHolder().key(), new ItemStackTemplate(Items.ZOMBIE_HEAD), false)
                .add(EntityType.PLAYER.builtInRegistryHolder().key(), new ItemStackTemplate(Items.PLAYER_HEAD), false)
                .add(EntityType.ENDER_DRAGON.builtInRegistryHolder().key(), new ItemStackTemplate(Items.DRAGON_HEAD), false)
        ;

        builder(CTDataMaps.PULVERIZING_BLOCKS)
                .add(Blocks.COBBLESTONE.defaultBlockState().typeHolder(), Collections.singletonList(chanceResult(Blocks.GRAVEL, 1.0f)), false)
                .add(Blocks.GRAVEL.defaultBlockState().typeHolder(), Collections.singletonList(chanceResult(Blocks.SAND, 1.0f)), false)
                .add(Blocks.STONE_BRICKS.defaultBlockState().typeHolder(), Collections.singletonList(chanceResult(Blocks.CRACKED_STONE_BRICKS, 1.0f)), false)
        ;

        builder(CTDataMaps.TREASURE)
                .add(Blocks.GRAVEL.defaultBlockState().typeHolder(), Collections.singletonList(chanceResult(Items.FLINT, 0.8f)), false)
                .add(Blocks.SAND.defaultBlockState().typeHolder(), Collections.singletonList(chanceResult(Items.HEART_OF_THE_SEA, 0.01f)), false)

        ;
    }

    public ChanceResult chanceResult(ItemLike item, float chance) {
        return new ChanceResult(new ItemStackTemplate(item.asItem()), chance);
    }
}
