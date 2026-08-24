package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class TeleportingModifier extends Modifier {

    @Override
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event, ModifierData data, int toolLevel) {

        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof ServerPlayer player) {

            double distance = toolLevel * data.additionalValue().orElse(8.0);

            Vec3 startPos = player.getEyePosition();
            Vec3 lookVec = player.getLookAngle();
            Vec3 endPos = startPos.add(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance);

            BlockHitResult raytrace = player.level().clip(new ClipContext(
                    startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

            Vec3 finalTeleportPos;

            if (raytrace.getType() == HitResult.Type.BLOCK) {
                finalTeleportPos = raytrace.getLocation().subtract(lookVec.scale(0.5));
            } else {
                finalTeleportPos = endPos;
            }

            player.teleportTo(finalTeleportPos.x, finalTeleportPos.y, finalTeleportPos.z);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, player.getSoundSource(), 1.0F, 1.0F);

            player.getItemInHand(event.getHand()).hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getItemInHand(event.getHand())));
            player.swing(event.getHand());
            player.getCooldowns().addCooldown(player.getWeaponItem(), 20);
        }
    }
}