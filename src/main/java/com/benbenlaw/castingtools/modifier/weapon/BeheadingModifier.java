package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import static com.benbenlaw.castingtools.datamaps.CTDataMaps.BEHEADING_DROPS;


public class BeheadingModifier extends Modifier {

    @Override
    public void onMobDrops(LivingDropsEvent event, ModifierData data, int toolLevel) {
        LivingEntity deadEntity = event.getEntity();
        Level level = deadEntity.level();

        ItemStack beheadingDrop = null;

        if (deadEntity.getType().builtInRegistryHolder().getData(BEHEADING_DROPS) != null) {
            beheadingDrop = deadEntity.getType().builtInRegistryHolder().getData(BEHEADING_DROPS).create();
        }

        if (beheadingDrop != null) {
            popOutTheItem(level, deadEntity.blockPosition(), beheadingDrop.copy());
        }
    }

    public static void popOutTheItem(Level level, BlockPos blockPos, ItemStack itemStack) {

        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.1, 0.5).offsetRandom(level.getRandom(), 0.7F);
        ItemStack itemstack1 = itemStack.copy();
        ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack1);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

}