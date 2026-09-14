package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class RetaliationModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int level) {
        LivingEntity victim = event.getEntity();
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        if (attacker == victim) return;
        float chance = 0.15f * level;

        if (victim.getRandom().nextFloat() <= chance) {

            float damage = data.additionalValue().orElse(1.0d).floatValue() + level * 1.5f;

            if (victim.level() instanceof ServerLevel serverLevel) {
                MinecraftServer server = serverLevel.getServer();
                server.schedule(new TickTask(server.getTickCount() + 1, () -> {
                    if (attacker.isAlive() && !attacker.isRemoved()) {
                        attacker.hurt(attacker.damageSources().thorns(victim), damage);
                    }
                }));
            }
        }
    }

}