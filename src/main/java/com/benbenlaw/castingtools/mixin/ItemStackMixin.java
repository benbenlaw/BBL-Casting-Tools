package com.benbenlaw.castingtools.mixin;

import com.benbenlaw.castingtools.config.ToolModifiersConfig;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.*;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void casting$customDurabilityHandling(
            int amount,
            LivingEntity entity,
            EquipmentSlot slot,
            CallbackInfo ci
    ) {
        ItemStack tool = (ItemStack)(Object)this;

        if (!tool.isDamageableItem()) return;
        ModifierComponent comp = tool.get(CastingToolsDataComponent.MODIFIER_COMPONENT);

        if (comp != null) {
            if (comp.modifiers().containsKey(UNBREAKING.get().getId())) {

                ModifierData modifier = Objects.requireNonNull(MODIFIER_REGISTRY.getValue(
                        comp.modifiers().containsKey(UNBREAKING.get().getId()) ? UNBREAKING.get().getId() : null)).getData();
                int level = comp.modifiers().get(UNBREAKING.get().getId());
                float chance = level * modifier.additionalValue().get().floatValue();

                RandomSource random = (entity != null) ? entity.getRandom() : RandomSource.create();

                if (random.nextFloat() < chance) {
                    ci.cancel(); // Cancel the durability loss
                }
            }
        }

    }
}