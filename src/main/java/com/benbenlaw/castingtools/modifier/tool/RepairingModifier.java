package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class RepairingModifier extends Modifier {

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {
        if (event.getEntity().level().isClientSide()) return;

        if (stack.isDamaged()) {
            long gameTime = event.getEntity().level().getGameTime();
            if (data.additionalValue().isEmpty()) return;
            if (gameTime % getTicksBetweenRepairs(toolLevel, (int) Math.round(data.additionalValue().get())) == 0) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

    private int getTicksBetweenRepairs(int level, int tickReductionPerLevel) {
        int baseTickAtFirstLevel = 200;
        return Math.max(1, baseTickAtFirstLevel - tickReductionPerLevel * level);
    }
}
