package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.List;

public class SilkTouchModifier extends Modifier {

    @Override
    public void onBlockDrops(BlockDropsEvent event, ModifierData data, int toolLevel) {
        ServerLevel level = event.getLevel();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = event.getBlockEntity();
        Player player = (Player) event.getBreaker();
        ItemStack tool = event.getTool();

        ItemStack silkTool = tool.copy();

        silkTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.SILK_TOUCH), 1);

        event.setDroppedExperience(0);
        event.getDrops().clear();
        List<ItemStack> silkDrops = Block.getDrops(state, level, pos, blockEntity, player, silkTool);

        for (ItemStack drop : silkDrops) {
            Block.popResource(level, pos, drop);
        }
    }
}
