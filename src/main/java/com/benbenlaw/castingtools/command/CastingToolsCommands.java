package com.benbenlaw.castingtools.command;

import com.benbenlaw.castingtools.CastingTools;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class CastingToolsCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ToolCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
