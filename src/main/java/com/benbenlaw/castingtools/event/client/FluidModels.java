package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.fluids.CTFluids;
import com.benbenlaw.core.Core;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class FluidModels {

    @SubscribeEvent
    private static void registerFluidModels(RegisterFluidModelsEvent event) {

        var still = new Material(Core.identifier("block/molten_still"));
        var flowing = new Material(Core.identifier("block/molten_flow"));

        FluidModel.Unbaked moltenOmnithium = new FluidModel.Unbaked(still, flowing, null, state -> 0x6EAEC1FF, null);

        event.register(moltenOmnithium, CTFluids.MOLTEN_OMNITHIUM.getStillFluid(), CTFluids.MOLTEN_OMNITHIUM.getFlowingFluid());

    }
}
