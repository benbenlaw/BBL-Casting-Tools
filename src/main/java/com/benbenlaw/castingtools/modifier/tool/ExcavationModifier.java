package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.castingtools.event.ModifierEvents;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ExcavationModifier extends Modifier {

    @Override
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ModifierData data, int toolLevel) {
        if (toolLevel > 0) {
            ModifierEvents.lastHitDirectionMap.put(event.getEntity().getUUID(), event.getFace());
        }
    }

    @Override
    public void onBlockBreak(BreakBlockEvent event, ModifierData data, int toolLevel) {
        Player player = (Player) event.getPlayer();
        Level level = player.level();
        ItemStack toolStack = player.getMainHandItem();
        BlockEntity blockEntity = level.getBlockEntity(event.getPos());

        List<BlockPos> extraBlocks = getExtraBlocks(event.getPos(), player, level, event.getState(), toolLevel);

        for (BlockPos pos : extraBlocks) {
            BlockState state = level.getBlockState(pos);
            Block.dropResources(state, level, pos, blockEntity, player, toolStack);

            boolean blockDestroyed = level.destroyBlock(pos, false);
            if (blockDestroyed) {
                player.getMainHandItem().hurtAndBreak(1, event.getPlayer(), InteractionHand.MAIN_HAND);
            }
        }
    }

    public List<BlockPos> getExtraBlocks(BlockPos origin, Player player, Level level, BlockState originState, int toolLevel) {
        Direction face = ModifierEvents.lastHitDirectionMap.getOrDefault(player.getUUID(), Direction.DOWN);

        List<BlockPos> area = ModifierUtils.getExcavationPlane(origin, face, toolLevel);

        List<BlockPos> result = new ArrayList<>();

        for (BlockPos pos : area) {
            if (pos.equals(origin)) continue;

            BlockState state = level.getBlockState(pos);
            if (state.getDestroySpeed(level, pos) < 0) continue;

            float originHardness = originState.getDestroySpeed(level, origin);
            float targetHardness = state.getDestroySpeed(level, pos);

            if (targetHardness > originHardness * 1.5f) continue;

            result.add(pos);
        }

        return result;
    }

}