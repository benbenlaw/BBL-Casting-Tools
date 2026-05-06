package com.benbenlaw.castingtools.mixin;

import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.*;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "processDurabilityChange(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void casting$unbreakingModifier(int amount, ServerLevel level, LivingEntity player, CallbackInfoReturnable<Integer> cir) {
        ItemStack tool = (ItemStack)(Object)this;

        ModifierComponent comp = tool.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp == null) return;

        if (!comp.modifiers().containsKey(UNBREAKING.get().getId())) return;

        ModifierData modifier = Objects.requireNonNull(MODIFIER_REGISTRY.getValue(UNBREAKING.get().getId())).getData();

        int levelValue = comp.modifiers().get(UNBREAKING.get().getId());
        float chance = levelValue * modifier.additionalValue().get().floatValue();

        RandomSource random = (player != null) ? player.getRandom() : RandomSource.create();


        if (random.nextFloat() < chance) {
            cir.setReturnValue(0);
        }
    }
}