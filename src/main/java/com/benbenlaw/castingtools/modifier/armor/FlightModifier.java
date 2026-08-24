package com.benbenlaw.castingtools.modifier.armor;

import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class FlightModifier extends Modifier {

    @Override
    public void onPlayerTick(PlayerTickEvent.Post event, ItemStack stack, ModifierData data, int toolLevel) {

        Player player = event.getEntity();
        Level level = player.level();
        boolean isWorn = false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorStack = player.getItemBySlot(slot);

                var comp = armorStack.get(CTDataComponent.MODIFIER_COMPONENT);
                if (comp != null) {
                    isWorn = comp.modifiers().get(ModifierRegistry.FLIGHT.get().getId()) != null;
                }
            }
        }


        if (!player.isCreative() && !player.isSpectator() && isWorn) {
            player.addTag("casting_flight");
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
        else if (!player.isCreative() && !player.isSpectator() && !isWorn && player.getAbilities().mayfly && player.entityTags().contains("casting_flight")) {
            player.removeTag("casting_flight");
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();


        }
    }
}
