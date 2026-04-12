package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.BounceModifierHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.NeoForgeAttributeTypes;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class BouncyModifier extends Modifier {

    //Most of the logic is handler in BounceModifierHandler

    @Override
    public void onFalling(LivingFallEvent event, ItemStack stack, ModifierData data) {

        LivingEntity entity = event.getEntity();

        if (event.getDistance() <= 2.0f || entity.isCrouching()) {
            return;
        }

        Vec3 motion = entity.getDeltaMovement();

        double gravity = entity.getAttributeValue(Attributes.GRAVITY);

        double velocityY = Math.sqrt(entity.fallDistance * gravity);
        velocityY = Math.min(velocityY, 3.0);

        double speedBoost = data.additionalValue().orElse(1.30);

        Vec3 bounce = new Vec3(
                motion.x * speedBoost,
                velocityY,
                motion.z * speedBoost
        );

        BounceModifierHandler.addBounce(entity, bounce);
        event.setDamageMultiplier(0.0F);

        event.setCanceled(true);
    }

    @Override
    public void onPreHit(LivingDamageEvent.Pre event, ModifierData data, int toolLevel) {
        if (event.getSource().is(DamageTypes.FALL)) {
            event.setNewDamage(0.0f);
        }
    }

}
