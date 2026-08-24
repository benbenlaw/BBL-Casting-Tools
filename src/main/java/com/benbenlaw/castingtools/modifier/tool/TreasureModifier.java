package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.castingtools.datamaps.CTDataMaps;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.List;

public class TreasureModifier extends Modifier {

    @Override
    public void onBlockDrops(BlockDropsEvent event, ModifierData data, int toolLevel) {

        ServerLevel level = event.getLevel();
        BlockState state = event.getState();

        List<ChanceResult> chanceResults = state.typeHolder().getData(CTDataMaps.TREASURE);
        if (chanceResults == null) return;

        for (ChanceResult result : chanceResults) {
            ItemStack itemStack = result.rollOutput(level.getRandom());
            Block.popResource(level, event.getPos(), itemStack);
        }
    }
}