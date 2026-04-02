package com.benbenlaw.castingtools.mixin;

import com.benbenlaw.castingtools.config.ToolModifiersConfig;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.EXCAVATION;
import static com.benbenlaw.castingtools.modifier.ModifierRegistry.UNBREAKING;

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
                int level = comp.modifiers().get(UNBREAKING.get().getId());
                float chance = level * ToolModifiersConfig.unbreakingChancePerLevel.get();

                RandomSource random = (entity != null) ? entity.getRandom() : RandomSource.create();

                if (random.nextFloat() < chance) {
                    ci.cancel(); // Cancel the durability loss
                }
            }
        }

    }
}