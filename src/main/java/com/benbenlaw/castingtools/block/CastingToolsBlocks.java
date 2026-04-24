package com.benbenlaw.castingtools.block;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.custom.ControllerBlock;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CastingToolsItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class CastingToolsBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CastingTools.MOD_ID);


    public static final DeferredBlock<Block> MODIFIER = registerBlock("modifier",
            properties -> new ModifierBlock(properties
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> OMNITHIUM_BLOCK = registerBlock("omnithium_block",
            properties -> new Block(properties
                    .requiresCorrectToolForDrops()
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.NETHERITE_BLOCK)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        CastingToolsItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
}
