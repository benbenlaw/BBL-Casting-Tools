package com.benbenlaw.castingtools.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ModifierComponent(Map<Identifier, Integer> modifiers) {
    public ModifierComponent(Map<Identifier, Integer> modifiers) {
        this.modifiers = new HashMap<>(modifiers);
    }

    public static final Codec<ModifierComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("modifiers")
                            .forGetter(ModifierComponent::modifiers)
            ).apply(instance, ModifierComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierComponent> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT)
                    .map(ModifierComponent::new, component -> new HashMap<>(component.modifiers()))
                    .cast();
}