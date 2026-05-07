package com.benbenlaw.castingtools.data.custom;

import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ModifierBuilder {
    private final int maxLevel;
    private final int maxEnhancedLevel;
    private final int experienceCost;
    private final String displayName;
    private final String description;
    private final List<String> validItems = new ArrayList<>();

    // String-based ingredient storage
    private String ingredientString;
    private int ingredientCount = 1;

    private String fluidString;
    private int fluidAmount = 0;

    private Double additionalValue = null;
    private final List<Identifier> incompatibleModifiers = new ArrayList<>();
    private Optional<Identifier> associatedEnchantment = Optional.empty();
    private Consumer<ModifierBuilder> saveCallback;

    private ModifierBuilder(int maxLevel, int maxEnhancedLevel, int experienceCost, String displayName, String description) {
        this.maxLevel = maxLevel;
        this.maxEnhancedLevel = maxEnhancedLevel;
        this.experienceCost = experienceCost;
        this.displayName = displayName;
        this.description = description;
    }

    public static ModifierBuilder create(int maxLevel, int maxEnhancedLevel, int experienceCost, String displayName, String description) {
        return new ModifierBuilder(maxLevel,maxEnhancedLevel, experienceCost, displayName, description);
    }

    public ModifierBuilder validItem(String itemOrTag) {
        this.validItems.add(itemOrTag);
        return this;
    }

    // New String-based Ingredient method
    public ModifierBuilder ingredient(String itemOrTag, int count) {
        this.ingredientString = itemOrTag;
        this.ingredientCount = count;
        return this;
    }

    // New String-based Fluid method
    public ModifierBuilder fluid(String fluidOrTag, int amount) {
        this.fluidString = fluidOrTag;
        this.fluidAmount = amount;
        return this;
    }

    public ModifierBuilder additionalValue(double value) {
        this.additionalValue = value;
        return this;
    }

    public ModifierBuilder incompatibleModifier(Identifier modifierId) {
        this.incompatibleModifiers.add(modifierId);
        return this;
    }

    public ModifierBuilder associatedEnchantment(Identifier enchantment) {
        this.associatedEnchantment = Optional.of(enchantment);
        return this;
    }

    public ModifierBuilder setSaveCallback(Consumer<ModifierBuilder> callback) {
        this.saveCallback = callback;
        return this;
    }


    public ModifierData build() {
        return new ModifierData(
                maxLevel,
                maxEnhancedLevel,
                experienceCost,
                validItems,
                displayName,
                description,
                Optional.ofNullable(ingredientString),
                ingredientCount,
                Optional.ofNullable(fluidString),
                fluidAmount,
                Optional.ofNullable(additionalValue),
                incompatibleModifiers.isEmpty() ? Optional.empty() : Optional.of(incompatibleModifiers),
                associatedEnchantment
        );
    }

    public void save() {
        if (this.saveCallback != null) {
            this.saveCallback.accept(this);
        }
    }
}