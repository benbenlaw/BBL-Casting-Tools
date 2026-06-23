package com.benbenlaw.castingtools.block;

import com.benbenlaw.casting.block.CastingBlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CastingToolsCapabilities {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        //Modifier
        event.registerBlockEntity(Capabilities.Item.BLOCK, CastingToolsBlockEntities.MODIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CastingToolsBlockEntities.MODIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler());

    }
}
