package com.benbenlaw.castingtools.fluids;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.fluid.FluidDeferredRegister;
import com.benbenlaw.core.fluid.FluidRegistryObject;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class CTFluids {

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(CastingTools.MOD_ID);

    public static final FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> MOLTEN_OMNITHIUM = FLUIDS.register("molten_omnithium",
            renderProperties -> renderProperties.temperature(1000).texture(Core.identifier("block/liquid"),
                    Core.identifier("block/liquid_flow")).tint(0x6EAEC1FF));

}
