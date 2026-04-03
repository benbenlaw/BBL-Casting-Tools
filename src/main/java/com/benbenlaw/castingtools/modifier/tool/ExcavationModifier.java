package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.castingtools.event.ModifierEvents;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
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

    @Override
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ModifierData data, int toolLevel) {
        if (toolLevel > 0) {
            ModifierEvents.lastHitDirectionMap.put(event.getEntity().getUUID(), event.getFace());
        }
    }

}