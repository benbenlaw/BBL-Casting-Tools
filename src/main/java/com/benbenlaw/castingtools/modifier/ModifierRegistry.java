package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.any.*;
import com.benbenlaw.castingtools.modifier.armor.*;
import com.benbenlaw.castingtools.modifier.tool.*;
import com.benbenlaw.castingtools.modifier.weapon.*;
import com.benbenlaw.castingtools.modifier.weapon.ranged.InfintiyModifier;
import com.benbenlaw.castingtools.modifier.weapon.ranged.PowerModifier;
import com.benbenlaw.castingtools.modifier.weapon.ranged.PunchModifier;
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

    //Any
    public static final DeferredHolder<Modifier, UnbreakingModifier> UNBREAKING = MODIFIERS.register("unbreaking", UnbreakingModifier::new);
    public static final DeferredHolder<Modifier, RepairingModifier> REPAIRING = MODIFIERS.register("repairing", RepairingModifier::new);
    public static final DeferredHolder<Modifier, TorchPlacerModifier> TORCH_PLACER = MODIFIERS.register("torch_placer", TorchPlacerModifier::new);
    public static final DeferredHolder<Modifier, CobblestonePlacerModifier> COBBLESTONE_PLACER = MODIFIERS.register("cobblestone_placer", CobblestonePlacerModifier::new);
    public static final DeferredHolder<Modifier, SoulboundModifier> SOULBOUND = MODIFIERS.register("soulbound", SoulboundModifier::new);
    public static final DeferredHolder<Modifier, MagnetModifier> MAGNET = MODIFIERS.register("magnet", MagnetModifier::new);
    public static final DeferredHolder<Modifier, BouncyModifier> BOUNCY = MODIFIERS.register("bouncy", BouncyModifier::new);

    //Armor Modifiers
    public static final DeferredHolder<Modifier, ProtectionModifier> PROTECTION = MODIFIERS.register("protection", ProtectionModifier::new);
    public static final DeferredHolder<Modifier, NightVisionModifier> NIGHT_VISION = MODIFIERS.register("night_vision", NightVisionModifier::new);
    public static final DeferredHolder<Modifier, WaterBreathingModifier> WATER_BREATHING = MODIFIERS.register("water_breathing", WaterBreathingModifier::new);
    public static final DeferredHolder<Modifier, SpeedModifier> SPEED = MODIFIERS.register("speed", SpeedModifier::new);
    public static final DeferredHolder<Modifier, StickyModifier> STICKY = MODIFIERS.register("sticky", StickyModifier::new);
    public static final DeferredHolder<Modifier, FlightModifier> FLIGHT = MODIFIERS.register("flight", FlightModifier::new);
    public static final DeferredHolder<Modifier, RetaliationModifier> RETALIATION = MODIFIERS.register("retaliation", RetaliationModifier::new);
    public static final DeferredHolder<Modifier, JetsModifier> JETS = MODIFIERS.register("jets", JetsModifier::new);

    //Weapon Modifiers
    public static final DeferredHolder<Modifier, IgniteModifier> IGNITE = MODIFIERS.register("ignite", IgniteModifier::new );
    public static final DeferredHolder<Modifier, SharpnessModifier> SHARPNESS = MODIFIERS.register("sharpness", SharpnessModifier::new);
    public static final DeferredHolder<Modifier, BeheadingModifier> BEHEADING = MODIFIERS.register("beheading", BeheadingModifier::new);
    public static final DeferredHolder<Modifier, LifestealModifier> LIFESTEAL = MODIFIERS.register("lifesteal", LifestealModifier::new);
    public static final DeferredHolder<Modifier, KnockbackModifier> KNOCKBACK = MODIFIERS.register("knockback", KnockbackModifier::new);
    public static final DeferredHolder<Modifier, TeleportingModifier> TELEPORTING = MODIFIERS.register("teleporting", TeleportingModifier::new);
    public static final DeferredHolder<Modifier, LightningStrikeModifier> LIGHTNING_STRIKE = MODIFIERS.register("lightning_strike", LightningStrikeModifier::new);

    //Ranged
    public static final DeferredHolder<Modifier, PunchModifier> PUNCH = MODIFIERS.register("punch", PunchModifier::new);
    public static final DeferredHolder<Modifier, PowerModifier> POWER = MODIFIERS.register("power", PowerModifier::new);
    public static final DeferredHolder<Modifier, InfintiyModifier> INFINITY = MODIFIERS.register("infinity", InfintiyModifier::new);

    //Tool Modifiers
    public static final DeferredHolder<Modifier, FortuneModifier> FORTUNE = MODIFIERS.register("fortune", FortuneModifier::new);
    public static final DeferredHolder<Modifier, SilkTouchModifier> SILK_TOUCH = MODIFIERS.register("silk_touch", SilkTouchModifier::new);
    public static final DeferredHolder<Modifier, ExcavationModifier> EXCAVATION = MODIFIERS.register("excavation", ExcavationModifier::new);
    public static final DeferredHolder<Modifier, EfficiencyModifier> EFFICIENCY = MODIFIERS.register("efficiency", EfficiencyModifier::new);
    public static final DeferredHolder<Modifier, LootingModifier> LOOTING = MODIFIERS.register("looting", LootingModifier::new);
    public static final DeferredHolder<Modifier, PulverizingModifier> PULVERIZING = MODIFIERS.register("pulverizing", PulverizingModifier::new);
    public static final DeferredHolder<Modifier, TreasureModifier> TREASURE = MODIFIERS.register("treasure", TreasureModifier::new);
}