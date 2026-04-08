package com.benbenlaw.castingtools.modifier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Modifier {

    private ModifierData data;

    // ------------------------------
    // Event Hooks
    // ------------------------------

    public void onPostHit(LivingDamageEvent.Post event, ModifierData data, int toolLevel) {}
    public void onPreHit(LivingDamageEvent.Pre event, ModifierData data, int toolLevel) {}
    public void onBlockBreak(BlockEvent.BreakEvent event, ModifierData data, int toolLevel) {}
    public void onCalculateDrops(ItemStack fakeStack, ModifierData data, Level world, int toolLevel) {}
    public void onBreakSpeed(PlayerEvent.BreakSpeed event, ModifierData data, int toolLevel) {}
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ModifierData data, int toolLevel) {}
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event, ModifierData data, int toolLevel) {}
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event, ModifierData data, int toolLevel) {}
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {}
    public void onPlayerDrops(LivingDropsEvent event, ItemStack stack, ModifierData data, int toolLevel, int slot) {}
    public void onPlayerDeath(LivingDeathEvent event, ItemStack stack, ModifierData data, int toolLevel, int slot) {}
    public void onPlayerClone(PlayerEvent.Clone event, ModifierData data, int toolLevel) {}
    public void onMobDrops(LivingDropsEvent event, ModifierData data, int toolLevel) {}
    public boolean overridesLootTable(ItemStack stack, ModifierData data, int toolLevel) { return false; }

    // ------------------------------
    // ModifierData
    // ------------------------------

    public void setData(ModifierData data) {
        this.data = data;
    }

    public ModifierData getData() {
        return data;
    }

    // ------------------------------
    // Configurable Fields
    // ------------------------------

    public int getMaxLevel() {
        if (data != null) return data.maxLevel();
        return 1;
    }

    public int getExperienceCost() {
        if (data != null) return data.experienceCost();
        return 1000;
    }

    public Set<Item> getValidItems() {
        if (data != null && data.validItems() != null) {
            return data.validItems().stream()
                    .filter(s -> !s.startsWith("#"))
                    .map(s -> BuiltInRegistries.ITEM.getValue(Identifier.parse(s)))
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Set<TagKey<Item>> getValidTags() {
        if (data != null && data.validItems() != null) {
            return data.validItems().stream()
                    .filter(s -> s.startsWith("#"))
                    .map(s -> TagKey.create(Registries.ITEM, Identifier.parse(s.substring(1))))
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Optional<SizedIngredient> getIngredient() {
        return data.ingredient().map(input -> {
            Ingredient ingredient = null;

            if (input.startsWith("#")) {
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, Identifier.parse(input.substring(1)));

                var tagHolder = BuiltInRegistries.ITEM.get(tagKey);

                if (tagHolder.isPresent()) {
                    ingredient = Ingredient.of(tagHolder.get());
                }
            } else {
                Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(input));
                ingredient = Ingredient.of(item);
            }

            assert ingredient != null;
            return new SizedIngredient(ingredient, data.ingredientCount());
        });
    }

    public Optional<SizedFluidIngredient> getFluidIngredient() {
        if (data != null && data.fluid().isPresent()) {
            String input = data.fluid().get();
            int amount = data.fluidAmount();

            if (input.startsWith("#")) {
                TagKey<Fluid> tagKey = TagKey.create(Registries.FLUID, Identifier.parse(input.substring(1)));
                var tagHolder = BuiltInRegistries.FLUID.get(tagKey);
                return tagHolder.map(holders -> new SizedFluidIngredient(FluidIngredient.of(holders), amount));

            } else {
                Fluid fluid = BuiltInRegistries.FLUID.getValue(Identifier.parse(input));
                return Optional.of(SizedFluidIngredient.of(fluid, amount));
            }
        }
        return Optional.empty();
    }

    public Set<Modifier> getIncompatibleModifiers() {
        if (data != null && data.incompatibleModifiers().isPresent()) {
            return data.incompatibleModifiers().get().stream()
                    .map(ModifierRegistry.MODIFIER_REGISTRY::getValue)
                    .filter(Objects::nonNull) // Ensure we don't return null if an ID is wrong
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    // ------------------------------
    // Validation
    // ------------------------------

    public boolean isValid(ItemStack stack) {
        if (!getValidItems().isEmpty() && getValidItems().contains(stack.getItem())) return true;
        if (!getValidTags().isEmpty() && getValidTags().stream().anyMatch(stack::is)) return true;
        return false;
    }

    // ------------------------------
    // Identification & Display
    // ------------------------------

    public Identifier getId() {
        return ModifierRegistry.MODIFIER_REGISTRY.getKey(this);
    }

    public Component getDisplayName() {
        return Component.translatable(data.displayName());
    }

    public Component getDescription() {
        return Component.translatable(data.description());
    }

    protected String getTranslationKey() {
        return "modifier.castingtools." + getModifierName();
    }

    protected String getModifierName() {
        String name = this.getClass().getSimpleName().replace("Modifier", "");
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}