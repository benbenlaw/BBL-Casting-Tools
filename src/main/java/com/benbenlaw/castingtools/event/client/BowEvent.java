package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(modid = CastingTools.MOD_ID, value = Dist.CLIENT)
public class BowEvent {

    @SubscribeEvent
    public static void onBowDraw(ComputeFovModifierEvent event) {

        if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().is(CTItems.OMNITHIUM_BOW)) {
            float fovModifier = 1f;
            int ticksUsingItem = event.getPlayer().getTicksUsingItem();
            float scale = Math.min(ticksUsingItem / 20.0F, 1.0F);
            fovModifier *= 1.0F - Mth.square(scale) * 0.15F;
            event.setNewFovModifier(Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get().floatValue(), 1.0F, fovModifier));
        }
    }
}
