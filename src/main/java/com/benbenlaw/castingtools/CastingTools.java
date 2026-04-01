package com.benbenlaw.castingtools;

import com.benbenlaw.castingtools.block.CastingToolsBlockEntities;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.block.CastingToolsCapabilities;
import com.benbenlaw.castingtools.config.ToolModifiersConfig;
import com.benbenlaw.castingtools.config.WeaponModifiersConfig;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.CastingToolsItems;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.screen.CastingToolsMenuTypes;
import com.benbenlaw.castingtools.screen.ModifierScreen;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(CastingTools.MOD_ID)
public class CastingTools {
    public static final String MOD_ID = "castingtools";

    public CastingTools(IEventBus modEventBus, final ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.STARTUP, WeaponModifiersConfig.SPEC, "bbl/castingtools/modifiers/weapons.toml");
        modContainer.registerConfig(ModConfig.Type.STARTUP, ToolModifiersConfig.SPEC, "bbl/castingtools/modifiers/tools.toml");

        CastingToolsBlocks.BLOCKS.register(modEventBus);
        CastingToolsBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        CastingToolsItems.ITEMS.register(modEventBus);
        CastingToolsMenuTypes.MENUS.register(modEventBus);

        CastingToolsDataComponent.COMPONENTS.register(modEventBus);
        ModifierRegistry.MODIFIERS.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        CastingToolsCapabilities.registerCapabilities(event);
    }


    @EventBusSubscriber(modid = CastingTools.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(CastingToolsMenuTypes.MODIFIER_MENU.get(), ModifierScreen::new);
        }
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
