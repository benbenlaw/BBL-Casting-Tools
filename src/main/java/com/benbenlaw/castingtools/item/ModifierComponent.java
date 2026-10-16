package com.benbenlaw.castingtools.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ModifierComponent(Map<Identifier, Integer> modifiers, Map<Identifier, Integer> limits) {
    public ModifierComponent(Map<Identifier, Integer> modifiers, Map<Identifier, Integer> limits) {
        this.modifiers = new HashMap<>(modifiers);
        this.limits = new HashMap<>(limits);
    }

    public ModifierComponent(Map<Identifier, Integer> modifiers) {
        this(modifiers, Map.of());
    }

    public static final Codec<ModifierComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("modifiers")
                            .forGetter(ModifierComponent::modifiers),
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .optionalFieldOf("limits", Map.of())
                            .forGetter(ModifierComponent::limits)
            ).apply(instance, ModifierComponent::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Identifier, Integer>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT)
                    .<Map<Identifier, Integer>>map(map -> map, HashMap::new)
                    .cast();

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierComponent> STREAM_CODEC = StreamCodec.composite(
            MAP_STREAM_CODEC, ModifierComponent::modifiers,
            MAP_STREAM_CODEC, ModifierComponent::limits,
            ModifierComponent::new
    );

    public int activeLevel(Identifier id) {
        Integer installed = modifiers.get(id);
        if (installed == null || installed <= 0) return 0;
        Integer limit = limits.get(id);
        return limit == null ? installed : Math.max(0, Math.min(installed, limit));
    }

    public Map<Identifier, Integer> activeModifiers() {
        Map<Identifier, Integer> result = new HashMap<>();
        for (Identifier id : modifiers.keySet()) {
            int level = activeLevel(id);
            if (level > 0) result.put(id, level);
        }
        return result;
    }

    public ModifierComponent withLimit(Identifier id, int level) {
        Map<Identifier, Integer> newLimits = new HashMap<>(limits);
        Integer installed = modifiers.get(id);
        if (installed == null || level >= installed) {
            newLimits.remove(id);
        } else {
            newLimits.put(id, Math.max(0, level));
        }
        return new ModifierComponent(modifiers, newLimits);
    }
}
