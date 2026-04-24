package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.utils.BiggerStreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.*;

public record ModifierData(
        int maxLevel,
        int maxEnhancedLevel,
        int experienceCost,
        List<String> validItems,
        String displayName,
        String description,
        Optional<String> ingredient,
        int ingredientCount,
        Optional<String> fluid,
        int fluidAmount,
        Optional<Double> additionalValue,
        Optional<List<Identifier>> incompatibleModifiers
) {

    public static final Codec<ModifierData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("max_level").forGetter(ModifierData::maxLevel),
            Codec.INT.fieldOf("max_enhanced_level").forGetter(ModifierData::maxEnhancedLevel),
            Codec.INT.fieldOf("experience_cost").forGetter(ModifierData::experienceCost),
            Codec.STRING.listOf().fieldOf("valid_items").forGetter(ModifierData::validItems),
            Codec.STRING.fieldOf("display_name").forGetter(ModifierData::displayName),
            Codec.STRING.fieldOf("description").forGetter(ModifierData::description),
            Codec.STRING.optionalFieldOf("ingredient").forGetter(ModifierData::ingredient),
            Codec.INT.optionalFieldOf("ingredient_count", 1).forGetter(ModifierData::ingredientCount),
            Codec.STRING.optionalFieldOf("fluid").forGetter(ModifierData::fluid),
            Codec.INT.optionalFieldOf("fluid_amount", 0).forGetter(ModifierData::fluidAmount),
            Codec.DOUBLE.optionalFieldOf("additional_value").forGetter(ModifierData::additionalValue),
            Identifier.CODEC.listOf().optionalFieldOf("incompatible_modifiers").forGetter(ModifierData::incompatibleModifiers)
    ).apply(instance, ModifierData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ModifierData::maxLevel,
            ByteBufCodecs.INT, ModifierData::maxEnhancedLevel,
            ByteBufCodecs.INT, ModifierData::experienceCost,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), ModifierData::validItems,
            ByteBufCodecs.STRING_UTF8, ModifierData::displayName,
            ByteBufCodecs.STRING_UTF8, ModifierData::description,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ModifierData::ingredient,
            ByteBufCodecs.VAR_INT, ModifierData::ingredientCount,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ModifierData::fluid,
            ByteBufCodecs.VAR_INT, ModifierData::fluidAmount,
            ByteBufCodecs.DOUBLE.apply(ByteBufCodecs::optional), ModifierData::additionalValue,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), ModifierData::incompatibleModifiers,
            ModifierData::new
    );

}