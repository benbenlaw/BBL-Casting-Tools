package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.config.ToolModifiersConfig;
import com.benbenlaw.castingtools.config.WeaponModifiersConfig;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.tool.ExcavationModifier;
import com.benbenlaw.castingtools.modifier.tool.FortuneModifier;
import com.benbenlaw.castingtools.modifier.tool.SilkTouchModifier;
import com.benbenlaw.castingtools.modifier.weapon.IgniteModifier;
import com.benbenlaw.castingtools.modifier.weapon.SharpnessModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModifierRegistry {
    public static final ResourceKey<Registry<Modifier>> KEY =
            ResourceKey.createRegistryKey(CastingTools.identifier("modifiers"));

    public static final DeferredRegister<Modifier> MODIFIERS =
            DeferredRegister.create(KEY, CastingTools.MOD_ID);

    public static final Registry<Modifier> REGISTRY = MODIFIERS.makeRegistry(builder -> builder.sync(true));



    //Weapon Modifiers
    public static final DeferredHolder<Modifier, IgniteModifier> IGNITE = MODIFIERS.register("ignite", () ->
            new IgniteModifier(WeaponModifiersConfig.igniteDurationPerLevel, WeaponModifiersConfig.igniteMaxLevel));

    public static final DeferredHolder<Modifier, SharpnessModifier> SHARPNESS = MODIFIERS.register("sharpness", () ->
            new SharpnessModifier(WeaponModifiersConfig.sharpnessDamagePerLevel, WeaponModifiersConfig.sharpnessMaxLevel));




    //Tool Modifiers
    public static final DeferredHolder<Modifier, FortuneModifier> FORTUNE = MODIFIERS.register("fortune", () ->
            new FortuneModifier(ToolModifiersConfig.fortuneMaxLevel));

    public static final DeferredHolder<Modifier, SilkTouchModifier> SILK_TOUCH = MODIFIERS.register("silk_touch", SilkTouchModifier::new);

    public static final DeferredHolder<Modifier, ExcavationModifier> EXCAVATION = MODIFIERS.register("excavation", () ->
            new ExcavationModifier(ToolModifiersConfig.excavationMaxLevel));



    //Helper
    public static Modifier getMatchingModifier(ItemStack toolStack, ItemStack ingredientStack) {
        // 1. First, check if the tool already has a modifier (Upgrade Logic)
        ModifierComponent comp = toolStack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null && !comp.modifiers().isEmpty()) {
            for (Identifier id : comp.modifiers().keySet()) {
                Modifier modifier = REGISTRY.getValue(id);
                if (modifier != null && modifier.isValid(toolStack)) {
                    // Check if the ingredient matches this specific existing modifier
                    if (modifier.getIngredient().isPresent() && modifier.getIngredient().get().test(ingredientStack)) {
                        return modifier;
                    }
                }
            }
        }

        // 2. If no existing modifier matches, check for NEW modifiers (Initial Application)
        for (Modifier modifier : REGISTRY) {
            if (modifier.isValid(toolStack)) {
                if (modifier.getIngredient().isPresent() && modifier.getIngredient().get().test(ingredientStack)) {
                    return modifier;
                }
            }
        }

        return null;
    }
}