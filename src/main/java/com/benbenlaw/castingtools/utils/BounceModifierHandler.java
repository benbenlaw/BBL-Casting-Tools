package com.benbenlaw.castingtools.utils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import javax.annotation.Nullable;
import java.util.IdentityHashMap;

public class BounceModifierHandler {

  private BounceModifierHandler() {}

  private static final IdentityHashMap<Entity, BounceInfo> MAP = new IdentityHashMap<>();

  public static void init() {
    NeoForge.EVENT_BUS.addListener(BounceModifierHandler::onLivingTick);
    NeoForge.EVENT_BUS.addListener(BounceModifierHandler::onServerStop);
  }

  public static void addBounce(LivingEntity entity, Vec3 bounce) {
    if (entity instanceof FakePlayer) return;

    BounceInfo info = MAP.computeIfAbsent(entity, e -> new BounceInfo());

    info.bounce = bounce;
    info.graceTicks = 2;
  }

  @SubscribeEvent
  public static void onLivingTick(PlayerTickEvent.Pre event) {

    LivingEntity entity = event.getEntity();
    BounceInfo info = MAP.get(entity);

    if (info == null) return;

    if (entity.isRemoved() || entity.isSpectator() || entity.isFallFlying()) {
      MAP.remove(entity);
      return;
    }

    boolean onGround = entity.onGround();
    if (info.graceTicks > 0 && info.bounce != null) {

      entity.setDeltaMovement(info.bounce);
      entity.fallDistance = 0;

      entity.setOnGround(false);
      entity.applyPostImpulseGraceTime(5);

      info.graceTicks--;
      if (info.graceTicks == 1) {
        entity.playSound(SoundEvents.SLIME_BLOCK_PLACE, 0.5f, 1.0f);
      }
    }

    if (onGround && info.graceTicks <= 0) {
      info.bounce = null;
    }

    if (onGround && info.bounce == null) {
      MAP.remove(entity);
    }
  }

  private static void onServerStop(ServerStoppingEvent event) {
    MAP.clear();
  }

  public static class BounceInfo {
    @Nullable
    Vec3 bounce;

    int graceTicks = 0;
  }
}