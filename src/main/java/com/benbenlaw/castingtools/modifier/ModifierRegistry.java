package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.armor.ProtectionModifier;
import com.benbenlaw.castingtools.modifier.tool.*;
import com.benbenlaw.castingtools.modifier.weapon.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModifierRegistry {
    public static final ResourceKey<Registry<Modifier>> KEY =
            ResourceKey.createRegistryKey(Identifier.parse("modifiers"));

    public static final ResourceKey<Registry<ModifierData>> MODIFIER_DATA_KEY =
            ResourceKey.createRegistryKey(Identifier.parse("modifier"));

    public static final DeferredRegister<Modifier> MODIFIERS =
            DeferredRegister.create(KEY, CastingTools.MOD_ID);

    public static final DeferredRegister<ModifierData> MODIFIER_DATA =
            DeferredRegister.create(MODIFIER_DATA_KEY, CastingTools.MOD_ID);

    public static final Registry<Modifier> MODIFIER_REGISTRY = MODIFIERS.makeRegistry(builder ->
            builder.sync(true)
    );

    public static final Registry<ModifierData> DATA_REGISTRY = MODIFIER_DATA.makeRegistry(builder ->
            builder.sync(true)
    );

    //Armor Modifiers
    public static final DeferredHolder<Modifier, ProtectionModifier> PROTECTION = MODIFIERS.register("protection", ProtectionModifier::new);

    //Weapon Modifiers
    public static final DeferredHolder<Modifier, IgniteModifier> IGNITE = MODIFIERS.register("ignite", IgniteModifier::new );
    public static final DeferredHolder<Modifier, SharpnessModifier> SHARPNESS = MODIFIERS.register("sharpness", SharpnessModifier::new);
    public static final DeferredHolder<Modifier, BeheadingModifier> BEHEADING = MODIFIERS.register("beheading", BeheadingModifier::new);
    public static final DeferredHolder<Modifier, LifestealModifier> LIFESTEAL = MODIFIERS.register("lifesteal", LifestealModifier::new);
    public static final DeferredHolder<Modifier, KnockbackModifier> KNOCKBACK = MODIFIERS.register("knockback", KnockbackModifier::new);
    public static final DeferredHolder<Modifier, TeleportingModifier> TELEPORTING = MODIFIERS.register("teleporting", TeleportingModifier::new);

    //Tool Modifiers
    public static final DeferredHolder<Modifier, FortuneModifier> FORTUNE = MODIFIERS.register("fortune", FortuneModifier::new);
    public static final DeferredHolder<Modifier, SilkTouchModifier> SILK_TOUCH = MODIFIERS.register("silk_touch", SilkTouchModifier::new);
    public static final DeferredHolder<Modifier, ExcavationModifier> EXCAVATION = MODIFIERS.register("excavation", ExcavationModifier::new);
    public static final DeferredHolder<Modifier, EfficiencyModifier> EFFICIENCY = MODIFIERS.register("efficiency", EfficiencyModifier::new);
    public static final DeferredHolder<Modifier, UnbreakingModifier> UNBREAKING = MODIFIERS.register("unbreaking", UnbreakingModifier::new);
    public static final DeferredHolder<Modifier, RepairingModifier> REPAIRING = MODIFIERS.register("repairing", RepairingModifier::new);
    public static final DeferredHolder<Modifier, TorchPlacerModifier> TORCH_PLACER = MODIFIERS.register("torch_placer", TorchPlacerModifier::new);
    public static final DeferredHolder<Modifier, LootingModifier> LOOTING = MODIFIERS.register("looting", LootingModifier::new);
}