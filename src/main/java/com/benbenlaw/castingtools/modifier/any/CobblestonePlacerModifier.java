package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class CobblestonePlacerModifier extends Modifier {

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event, ModifierData data, int toolLevel) {
        Level world = event.getLevel();
        if (world.isClientSide()) return;

        BlockPos pos = event.getPos();
        Direction face = event.getFace();
        Player player = event.getEntity();
        ItemStack tool = event.getItemStack();

        if (face == null) return;

        BlockPos placePos = pos.relative(face);

        if (!world.getBlockState(placePos).canBeReplaced()) return;

        BlockState stateToPlace = Blocks.COBBLESTONE.defaultBlockState();

        boolean success = world.setBlockAndUpdate(placePos, stateToPlace);

        if (success) {
            world.playSound(null, placePos, Blocks.COBBLESTONE.getSoundType(stateToPlace, world, pos, player).getPlaceSound(),
                    SoundSource.BLOCKS, 1.0F, 1.0F);

            tool.hurtAndBreak(1, player, event.getHand());
            player.swing(event.getHand());

            event.setCancellationResult(InteractionResult.SUCCESS_SERVER);
            event.setCanceled(true);
        }

    }




}