package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.config.ToolModifiersConfig;
import com.benbenlaw.castingtools.config.WeaponModifiersConfig;
import com.benbenlaw.castingtools.modifier.tool.*;
import com.benbenlaw.castingtools.modifier.weapon.IgniteModifier;
import com.benbenlaw.castingtools.modifier.weapon.SharpnessModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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

    public static final DeferredHolder<Modifier, EfficiencyModifier> EFFICIENCY = MODIFIERS.register("efficiency", () ->
            new EfficiencyModifier(ToolModifiersConfig.efficiencyMiningSpeedPerLevel, ToolModifiersConfig.efficiencyMaxLevel));

    public static final DeferredHolder<Modifier, UnbreakingModifier> UNBREAKING = MODIFIERS.register("unbreaking", () ->
            new UnbreakingModifier(ToolModifiersConfig.unbreakingChancePerLevel, ToolModifiersConfig.unbreakingMaxLevel));

    public static final DeferredHolder<Modifier, RepairingModifier> REPAIRING = MODIFIERS.register("repairing", () ->
            new RepairingModifier(ToolModifiersConfig.repairingBaseTickAtFirstLevel, ToolModifiersConfig.repairingTickReductionMaxLevel, ToolModifiersConfig.repairingMaxLevel));

    public static final DeferredHolder<Modifier, TorchPlacerModifier> TORCH_PLACER = MODIFIERS.register("torch_placer", TorchPlacerModifier::new);

    public static final DeferredHolder<Modifier, LootingModifier> LOOTING = MODIFIERS.register("looting", () ->
            new LootingModifier(ToolModifiersConfig.lootingMaxLevel));
}