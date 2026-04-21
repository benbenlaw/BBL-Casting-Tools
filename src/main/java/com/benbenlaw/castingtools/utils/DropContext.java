package com.benbenlaw.castingtools.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class DropContext {

    public final Level level;
    public final Player player;
    public final BlockPos pos;
    public final BlockState state;
    public final ItemStack fakeTool;

    public DropMode mode = DropMode.NORMAL;
    public boolean cancelVanilla = false;

    public final Map<String, Object> extras = new HashMap<>();

    public DropContext(Level level, Player player, BlockPos pos, BlockState state, ItemStack fakeTool) {
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.state = state;
        this.fakeTool = fakeTool;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtra(String key) {
        return (T) extras.get(key);
    }

    public void setExtra(String key, Object value) {
        extras.put(key, value);
    }
}
