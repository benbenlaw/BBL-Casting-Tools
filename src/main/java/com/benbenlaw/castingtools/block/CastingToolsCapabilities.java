package com.benbenlaw.castingtools.block;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CastingToolsCapabilities {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        //Modifier
        event.registerBlockEntity(Capabilities.Item.BLOCK, CTBlockEntities.MODIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getAutomationItemHandler());

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CTBlockEntities.MODIFIER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler());

        //Upgrader
        event.registerBlockEntity(Capabilities.Item.BLOCK, CTBlockEntities.UPGRADER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getAutomationItemHandler());

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, CTBlockEntities.UPGRADER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler());

    }
}
