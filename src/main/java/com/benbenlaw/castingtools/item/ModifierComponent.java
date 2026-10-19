package com.benbenlaw.castingtools.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ModifierComponent(Map<Identifier, Integer> modifiers, Map<Identifier, Integer> limits, Map<Identifier, Integer> keybinds, Map<Identifier, Integer> restore) {

    public ModifierComponent(Map<Identifier, Integer> modifiers, Map<Identifier, Integer> limits,
                             Map<Identifier, Integer> keybinds, Map<Identifier, Integer> restore) {
        this.modifiers = new HashMap<>(modifiers);
        this.limits = new HashMap<>(limits);
        this.keybinds = new HashMap<>(keybinds);
        this.restore = new HashMap<>(restore);
    }

    public ModifierComponent(Map<Identifier, Integer> modifiers) {
        this(modifiers, Map.of(), Map.of(), Map.of());
    }

    public static final Codec<ModifierComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("modifiers")
                            .forGetter(ModifierComponent::modifiers),
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .optionalFieldOf("limits", Map.of())
                            .forGetter(ModifierComponent::limits),
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .optionalFieldOf("keybinds", Map.of())
                            .forGetter(ModifierComponent::keybinds),
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .optionalFieldOf("restore", Map.of())
                            .forGetter(ModifierComponent::restore)
            ).apply(instance, ModifierComponent::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Identifier, Integer>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT)
                    .<Map<Identifier, Integer>>map(map -> map, HashMap::new)
                    .cast();

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierComponent> STREAM_CODEC = StreamCodec.composite(
            MAP_STREAM_CODEC, ModifierComponent::modifiers,
            MAP_STREAM_CODEC, ModifierComponent::limits,
            MAP_STREAM_CODEC, ModifierComponent::keybinds,
            MAP_STREAM_CODEC, ModifierComponent::restore,
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

    public int keybind(Identifier id) {
        return keybinds.getOrDefault(id, 0);
    }

    public ModifierComponent withModifiers(Map<Identifier, Integer> newModifiers) {
        return new ModifierComponent(newModifiers, limits, keybinds, restore);
    }

    public ModifierComponent withLimit(Identifier id, int level) {
        Map<Identifier, Integer> newRestore = new HashMap<>(restore);
        newRestore.remove(id);
        return new ModifierComponent(modifiers, limitsWith(id, level), keybinds, newRestore);
    }

    public ModifierComponent toggled(Identifier id) {
        Integer installed = modifiers.get(id);
        if (installed == null) return this;

        Map<Identifier, Integer> newRestore = new HashMap<>(restore);
        int current = activeLevel(id);
        if (current > 0) {
            newRestore.put(id, current);
            return new ModifierComponent(modifiers, limitsWith(id, 0), keybinds, newRestore);
        }

        int back = newRestore.getOrDefault(id, installed);
        newRestore.remove(id);
        return new ModifierComponent(modifiers, limitsWith(id, Math.max(1, back)), keybinds, newRestore);
    }

    private Map<Identifier, Integer> limitsWith(Identifier id, int level) {
        Map<Identifier, Integer> newLimits = new HashMap<>(limits);
        Integer installed = modifiers.get(id);
        if (installed == null || level >= installed) {
            newLimits.remove(id);
        } else {
            newLimits.put(id, Math.max(0, level));
        }
        return newLimits;
    }

    public ModifierComponent withKeybind(Identifier id, int keyCode) {
        Map<Identifier, Integer> newKeybinds = new HashMap<>(keybinds);
        if (keyCode <= 0) {
            newKeybinds.remove(id);
        } else {
            newKeybinds.put(id, keyCode);
        }
        return new ModifierComponent(modifiers, limits, newKeybinds, restore);
    }
}
