package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.castingtools.datamaps.CastingToolsDataMaps;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PulverizingModifier extends Modifier {

    @Override
    public void onBlockDrops(BlockDropsEvent event, ModifierData data, int toolLevel) {

        ServerLevel level = event.getLevel();
        BlockState state = event.getState();
        Player player = (Player) event.getBreaker();
        ItemStack tool = event.getTool();

        Identifier lootTableId = state.typeHolder().getData(CastingToolsDataMaps.PULVERIZING_BLOCKS);

        if (lootTableId == null) return;
        ResourceKey<LootTable> key =ResourceKey.create(Registries.LOOT_TABLE, lootTableId);

        event.getDrops().clear();
        assert player != null;
        List<ItemStack> pulverizingDrops = level.getServer()
                .reloadableRegistries()
                .getLootTable(key)
                .getRandomItems(new LootParams.Builder(level)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(player.blockPosition()))
                        .withParameter(LootContextParams.BLOCK_STATE, state)
                        .withParameter(LootContextParams.TOOL, tool)
                        .create(LootContextParamSets.BLOCK));

        for (ItemStack drop : pulverizingDrops) {
            Block.popResource(level, event.getPos(), drop);
        }
    }
}