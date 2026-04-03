package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.data.custom.FluidStackTemplateHelper;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;

public class SilkTouchModifier extends Modifier {

    @Override
    public void onCalculateDrops(ItemStack fakeStack, ModifierData data, Level world, int toolLevel) {
        if (toolLevel > 0) {
            fakeStack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.SILK_TOUCH), 1);
        }
    }
}