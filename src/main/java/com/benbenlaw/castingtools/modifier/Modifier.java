package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class Modifier {



    //Used to apply post damage effects, fire for example
    public void onPostHit(LivingDamageEvent.Post event, int level) {}

    //Used to modify the damage being dealt or taken, for example sharpness
    public void onDamageCalculate(LivingDamageEvent.Pre event, int level) {}

    //Used to apply modifier after breaking a block, fortune and silk touch for example
    public void onBlockBreak(BlockEvent.BreakEvent event, int level) {}

    //Calculates drops for blocks, used for fortune
    public void onCalculateDrops(ItemStack fakeStack, int level, Level world) {}

    //Sized Ingredient for this modifier
    public Optional<SizedIngredient> getIngredient() {
        return Optional.empty();
    }

    //Sized Fluid Ingredient for this modifier
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.empty();
    }

    //Incompatible modifiers that cannot be applied together, for example fortune and silk touch
    public Set<Modifier> getIncompatibleModifiers() {
        return Set.of();
    }

    //Max level for the modifier
    public int getMaxLevel() {
        return 1;
    }

    //Molten Experience cost for this modifier
    public int getExperienceCost() {
        return 1000;
    }

    //Valid Tags for Modifier
    public Set<TagKey<Item>> getValidTags() {
        return Set.of();
    }

    //Valid Items for Modifier
    public Set<Item> getValidItems() {
        return Set.of();
    }

    public boolean isValid(ItemStack stack) {
        if (!getValidItems().isEmpty()) {
            return getValidItems().contains(stack.getItem());
        }

        if (!getValidTags().isEmpty()) {
            return getValidTags().stream().anyMatch(stack::is);
        }

        return false;
    }

    //Modifier ID should match the ModifierRegistry ID, used for actually applying the modifier
    public Identifier getId() {
        return ModifierRegistry.REGISTRY.getKey(this);
    }

    //Name
    public Component getDisplayName() {
        return Component.translatable(getTranslationKey());
    }

    //Description, shown in the modifier application screen
    public Component getDescription() {
        return Component.translatable(getTranslationKey() + ".description");
    }

    protected String getTranslationKey() {
        return "modifier.castingtools." + getModifierName();
    }

    protected String getModifierName() {
        String name = this.getClass().getSimpleName().replace("Modifier", "");

        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }


}