package com.benbenlaw.castingtools.block;

import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.castingtools.block.entity.ConfiguratorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfiguratorBlock extends CastingBlock {

    public static final MapCodec<ConfiguratorBlock> CODEC = simpleCodec(ConfiguratorBlock::new);

    @Override
    protected @NotNull MapCodec<ConfiguratorBlock> codec() {
        return CODEC;
    }

    public ConfiguratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ConfiguratorBlockEntity configurator) {
                player.openMenu(new SimpleMenuProvider(configurator, configurator.getDisplayName()), pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ConfiguratorBlockEntity(pos, state);
    }
}
