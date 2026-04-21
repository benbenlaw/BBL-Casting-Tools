package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.castingtools.datamaps.CastingToolsDataMaps;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PulverizingModifier extends Modifier {

    @Override
    public void onBlockBreak(BreakBlockEvent event, ModifierData data, int toolLevel) {

        Level level = (Level) event.getLevel();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        if (!(level instanceof ServerLevel serverLevel)) return;

        Identifier lootTableId =
                state.typeHolder().getData(CastingToolsDataMaps.PULVERIZING_BLOCKS);

        if (lootTableId == null) return;
        ResourceKey<LootTable> key =ResourceKey.create(Registries.LOOT_TABLE, lootTableId);

        dropFromLootTable(serverLevel, key, player, state, (lvl, stack) -> Block.popResource(level, event.getPos(), stack));

        //Remove Block
        level.setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        level.destroyBlock(event.getPos(), false, player);

    }

    protected boolean dropFromLootTable(ServerLevel level, ResourceKey<LootTable> key, Player player, BlockState state, BiConsumer<ServerLevel, ItemStack> consumer) {
        LootTable lootTable = level.getServer()
                .reloadableRegistries()
                .getLootTable(key);

        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(player.blockPosition()))
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .create(LootContextParamSets.BLOCK);

        List<ItemStack> drops = lootTable.getRandomItems(params);

        if (!drops.isEmpty()) {
            drops.forEach(stack -> consumer.accept(level, stack));
            return true;
        }

        return false;
    }
}