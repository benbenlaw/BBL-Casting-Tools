package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jspecify.annotations.Nullable;

public class ExperienceBoostModifier extends Modifier {

    @Override
    public void onBlockDrops(BlockDropsEvent event, ModifierData data, int toolLevel) {
        int originalAmount = event.getDroppedExperience();

        if (originalAmount > 0) {
            int newAmount = (int) (originalAmount * (1 + data.additionalValue().orElse(0.25).floatValue() * toolLevel));
            event.setDroppedExperience(newAmount);
        }
    }

    @Override
    public void onExperienceDropped(LivingExperienceDropEvent event, ModifierData data, int toolLevel) {
        int originalAmount = event.getOriginalExperience();
        if (originalAmount > 0) {
            int newAmount = (int) (originalAmount * (1 + data.additionalValue().orElse(0.25).floatValue() * toolLevel));
            event.setDroppedExperience(newAmount);
        }
    }
}
