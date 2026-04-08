package com.benbenlaw.castingtools.utils;

import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModifierUtils {

    public static void breakBlockWithCasting(Level level, Player player, BlockPos pos, ItemStack tool) {
        if (level.isClientSide()) return;

        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        AtomicBoolean isSilkTouch = new AtomicBoolean(false);

        //Prepare Fake Tool for Silk Touch and Fortune as these affects drops
        ItemStack fakeItemStack = tool.copy();
        ModifierComponent comp = tool.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null) {
            comp.modifiers().forEach((key, modifierLevel) -> {
                Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(key);
                if (modifier != null) {
                    modifier.onCalculateDrops(fakeItemStack, modifier.getData(), level, modifierLevel);
                }
                if (modifier == ModifierRegistry.SILK_TOUCH.get()) {
                    isSilkTouch.set(true);
                }
            });
        }

        //Get Experience Drop
        int blockExperience = 0;
        if (state.getBlock() instanceof DropExperienceBlock experienceBlock) {
            blockExperience = experienceBlock.getExpDrop(state, level, pos, blockEntity, player, fakeItemStack);
        }

        //Get and Spawn Drops
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, blockEntity, player, fakeItemStack);
        for (ItemStack drop : drops) {
            Block.popResource(level, pos, drop);
        }

        //Remove Block
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        level.destroyBlock(pos, true, player);

        //Drop Experience
        if (blockExperience > 0 && !isSilkTouch.get()) {
            level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockExperience));
        }

        //Damage Tool
        tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
    }


    public static List<BlockPos> getExcavationPlane(BlockPos origin, Direction face, int level) {
        List<BlockPos> positions = new ArrayList<>();

        Direction.Axis axis = face.getAxis();
        Direction.Axis axis1;
        Direction.Axis axis2;

        switch (axis) {
            case X -> {
                axis1 = Direction.Axis.Y;
                axis2 = Direction.Axis.Z;
            }
            case Y -> {
                axis1 = Direction.Axis.X;
                axis2 = Direction.Axis.Z;
            }
            case Z -> {
                axis1 = Direction.Axis.X;
                axis2 = Direction.Axis.Y;
            }
            default -> throw new IllegalStateException("Unexpected axis: " + axis);
        }

        for (int i = -level; i <= level; i++) {
            for (int j = -level; j <= level; j++) {
                BlockPos offset = origin;

                // Apply offset based on the perpendicular axes
                offset = offset.relative(Direction.fromAxisAndDirection(axis1, i >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE), Math.abs(i));
                offset = offset.relative(Direction.fromAxisAndDirection(axis2, j >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE), Math.abs(j));

                positions.add(offset);
            }
        }

        return positions;
    }


    public static int getModifierLevel(ItemStack stack, Modifier modifier) {
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null) {
            Integer level = comp.modifiers().get(modifier.getId());
            if (level != null && level > 0) {
                return level;
            }
        }
        return 0;
    }

    public static Modifier getMatchingModifier(ItemStack toolStack, ItemStack ingredientStack, FluidStack tankFluid) {

        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (!modifier.isValid(toolStack)) continue;

            var itemIng = modifier.getIngredient();
            var fluidIng = modifier.getFluidIngredient();

            if (itemIng.isPresent() && fluidIng.isPresent()) {
                if (itemIng.get().test(ingredientStack) && fluidIng.get().test(tankFluid)) {
                    return modifier;
                }
            }
        }

        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (!modifier.isValid(toolStack)) continue;

            var itemIng = modifier.getIngredient();
            var fluidIng = modifier.getFluidIngredient();

            if (itemIng.isPresent() && fluidIng.isEmpty()) {
                if (itemIng.get().test(ingredientStack)) {
                    return modifier;
                }
            }
        }

        for (Modifier modifier : ModifierRegistry.MODIFIER_REGISTRY) {
            if (!modifier.isValid(toolStack)) continue;

            var itemIng = modifier.getIngredient();
            var fluidIng = modifier.getFluidIngredient();

            if (fluidIng.isPresent() && itemIng.isEmpty()) {
                if (fluidIng.get().test(tankFluid) && ingredientStack.isEmpty()) {
                    return modifier;
                }
            }
        }

        return null;
    }

    public static void setModifierLevel(ItemStack stack, Modifier modifier, int level) {
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        Map<Identifier, Integer> map = (comp == null) ? new HashMap<>() : new HashMap<>(comp.modifiers());
        map.put(modifier.getId(), level);
        stack.set(CastingToolsDataComponent.MODIFIER_COMPONENT, new ModifierComponent(map));
    }

    public static boolean hasConflict(ItemStack stack, Modifier newModifier) {
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null) {
            for (Identifier modifierId : comp.modifiers().keySet()) {
                Modifier existingModifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(modifierId);
                if (existingModifier != null) {

                    if (existingModifier.getIncompatibleModifiers().contains(newModifier) ||
                        newModifier.getIncompatibleModifiers().contains(existingModifier)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasSoulbound(ItemStack stack) {
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null) {
            return comp.modifiers().containsKey(ModifierRegistry.SOULBOUND.get().getId());
        }
        return false;
    }


}
