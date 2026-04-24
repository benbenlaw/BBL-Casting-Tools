package com.benbenlaw.castingtools.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.utils.CTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CTBlockTags extends BlockTagsProvider {

    CTBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Casting.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CastingToolsBlocks.MODIFIER.get())
                .add(CastingToolsBlocks.OMNITHIUM_BLOCK.get())
        ;

        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(CastingToolsBlocks.OMNITHIUM_BLOCK.get());
        tag(CTTags.Blocks.OMNITHIUM_BLOCK).add(CastingToolsBlocks.OMNITHIUM_BLOCK.get());

    }

}
