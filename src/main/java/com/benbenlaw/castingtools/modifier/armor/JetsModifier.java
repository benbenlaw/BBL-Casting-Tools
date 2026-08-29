package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class JetsModifier extends Modifier {

    @Override
    public void onPlayerEveryTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {

        Player player = event.getEntity();
        Level level = player.level();
        int totalJetsLevel = 0;

        boolean jumpHeld = player.getPersistentData().getBooleanOr("casting_is_jumping", true);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorStack = player.getItemBySlot(slot);

                var comp = armorStack.get(CTDataComponent.MODIFIER_COMPONENT);
                if (comp != null) {
                    if (comp.modifiers().get(ModifierRegistry.JETS.get().getId()) != null) {
                        totalJetsLevel += comp.modifiers().get(ModifierRegistry.JETS.get().getId());
                    }
                }
            }
        }

        if (totalJetsLevel > 0 && jumpHeld) {

            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, totalJetsLevel, true, false, false));
            player.fallDistance = 0;

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 0.25, player.getZ(),
                        5, 0.2, 0.2, 0.2, 0.001);
            }
        }
    }
}
