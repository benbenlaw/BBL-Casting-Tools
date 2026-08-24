package com.benbenlaw.castingtools.mixin;

import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(
            method = "getTagEnchantmentLevel",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void castingTools$getTagEnchantmentLevel(Holder<Enchantment> enchantment, ItemInstance piece, CallbackInfoReturnable<Integer> cir) {

        ModifierComponent comp =piece.get(CTDataComponent.MODIFIER_COMPONENT);

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

        ModifierComponent comp =piece.get(CTDataComponent.MODIFIER_COMPONENT);

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
            method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V",
            at = @At("TAIL")
    )
    private static void castingTools$runIterationOnItem(ItemStack piece, EnchantmentHelper.EnchantmentVisitor method, CallbackInfo ci) {

        ModifierComponent comp = piece.get(CTDataComponent.MODIFIER_COMPONENT);
        if (comp == null) {
            return;
        }

        HolderLookup.RegistryLookup<Enchantment> lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup == null) {
            return;
        }

        for (var entry : comp.modifiers().entrySet()) {

            Identifier id = entry.getKey();
            int level = entry.getValue();

            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
            if (modifier == null) {
                continue;
            }

            Optional<Identifier> associatedEnchantment = modifier.getAssociatedEnchantment();
            if (associatedEnchantment.isEmpty()) {
                continue;
            }

            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, associatedEnchantment.get());
            Optional<Holder.Reference<Enchantment>> holder = lookup.get(key);

            holder.ifPresent(enchHolder -> method.accept(enchHolder, level));
        }
    }

    @Inject(
            method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;)V",
            at = @At("TAIL")
    )
    private static void castingTools$runIterationOnItemSlot(ItemStack piece, EquipmentSlot slot, LivingEntity owner, EnchantmentHelper.EnchantmentInSlotVisitor method, CallbackInfo ci) {

        if (piece.isEmpty()) {
            return;
        }

        ModifierComponent comp = piece.get(CTDataComponent.MODIFIER_COMPONENT);
        if (comp == null) {
            return;
        }

        HolderLookup.RegistryLookup<Enchantment> lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup == null) {
            return;
        }

        EnchantedItemInUse itemInUse = null;

        for (var entry : comp.modifiers().entrySet()) {

            Identifier id = entry.getKey();
            int level = entry.getValue();

            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
            if (modifier == null) {
                continue;
            }

            Optional<Identifier> associatedEnchantment = modifier.getAssociatedEnchantment();
            if (associatedEnchantment.isEmpty()) {
                continue;
            }

            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, associatedEnchantment.get());
            Optional<Holder.Reference<Enchantment>> holder = lookup.get(key);
            if (holder.isEmpty()) {
                continue;
            }

            Holder<Enchantment> enchHolder = holder.get();
            if (!enchHolder.value().matchingSlot(slot)) {
                continue;
            }

            if (itemInUse == null) {
                itemInUse = new EnchantedItemInUse(piece, slot, owner);
            }

            method.accept(enchHolder, level, itemInUse);
        }
    }
}