package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.castingtools.event.ModifierEvents;
import com.benbenlaw.castingtools.modifier.Modifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ExcavationModifier extends Modifier {

    private final Supplier<Integer> maxLevel;

    public ExcavationModifier(Supplier<Integer> maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }

    @Override
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, int level) {
        if (level > 0) {
            ModifierEvents.lastHitDirectionMap.put(event.getEntity().getUUID(), event.getFace());
        }
    }

    @Override
    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return Optional.of(SizedFluidIngredient.of(BuiltInRegistries.FLUID.getValue(Casting.identifier("molten_diamond")), 360));
    }

    @Override
    public Set<TagKey<Item>> getValidTags() {
        return Set.of(
                ItemTags.PICKAXES,
                ItemTags.SHOVELS
        );
    }


}