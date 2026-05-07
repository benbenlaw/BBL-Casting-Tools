package com.benbenlaw.castingtools.mixin;

import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.EXCAVATION;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(
            method = "getTagEnchantmentLevel",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void castingTools$getTagEnchantmentLevel(Holder<Enchantment> enchantment, ItemInstance piece, CallbackInfoReturnable<Integer> cir) {

        ModifierComponent comp =piece.get(CastingToolsDataComponent.MODIFIER_COMPONENT);

        if (comp == null) {
            return;
        }

        int modifierLevel = 0;

        for (var entry : comp.modifiers().entrySet()) {

            Identifier id = entry.getKey();
            int level = entry.getValue();

            Modifier modifier =
                    ModifierRegistry.MODIFIER_REGISTRY.getValue(id);

            if (modifier == null) {
                continue;
            }

            Optional<Identifier> associatedEnchantment =
                    modifier.getAssociatedEnchantment();

            if (associatedEnchantment.isEmpty()) {
                continue;
            }

            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, associatedEnchantment.get());

            if (enchantment.is(key)) {
                modifierLevel += level;
            }
        }

        if (modifierLevel > 0) {
            cir.setReturnValue(cir.getReturnValue() + modifierLevel);
        }
    }

    @Inject(
            method = "getItemEnchantmentLevel",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void castingTools$getEnchantmentLevel(Holder<Enchantment> enchantment, ItemInstance piece, CallbackInfoReturnable<Integer> cir) {

        ModifierComponent comp =piece.get(CastingToolsDataComponent.MODIFIER_COMPONENT);

        if (comp == null) {
            return;
        }

        int modifierLevel = 0;

        for (var entry : comp.modifiers().entrySet()) {

            Identifier id = entry.getKey();
            int level = entry.getValue();

            Modifier modifier =
                    ModifierRegistry.MODIFIER_REGISTRY.getValue(id);

            if (modifier == null) {
                continue;
            }

            Optional<Identifier> associatedEnchantment =
                    modifier.getAssociatedEnchantment();

            if (associatedEnchantment.isEmpty()) {
                continue;
            }

            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, associatedEnchantment.get());

            if (enchantment.is(key)) {
                modifierLevel += level;
            }
        }

        if (modifierLevel > 0) {
            cir.setReturnValue(cir.getReturnValue() + modifierLevel);
        }
    }
}