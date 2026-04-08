package com.benbenlaw.castingtools.modifier.any;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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
