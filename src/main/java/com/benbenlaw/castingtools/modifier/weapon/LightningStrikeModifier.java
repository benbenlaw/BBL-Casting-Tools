package com.benbenlaw.castingtools.modifier.weapon;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;

public class LightningStrikeModifier extends Modifier {

    @Override
    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {

        Entity enemy = event.getEntity();
        Level level = event.getEntity().level();
        Vec3 position = event.getEntity().position();
        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);

        lightning.setDamage(0.0f);
        lightning.setPos(position);
        level.addFreshEntity(lightning);
        float finalDamage = (float) (event.getInflictedDamage() + (data.additionalValue().orElse(5.0) * toolLevel));

        //System.out.println("finalDamage: " + finalDamage);
        enemy.hurt(level.damageSources().lightningBolt(), finalDamage);
    }
}