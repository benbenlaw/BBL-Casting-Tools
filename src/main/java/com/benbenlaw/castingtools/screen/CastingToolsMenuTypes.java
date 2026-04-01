package com.benbenlaw.castingtools.screen;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.screen.ControllerMenu;
import com.benbenlaw.casting.screen.MixerMenu;
import com.benbenlaw.casting.screen.SolidifierMenu;
import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CastingToolsMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, CastingTools.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ModifierMenu>> MODIFIER_MENU =
            MENUS.register("modifier_menu", () -> IMenuTypeExtension.create(ModifierMenu::new));

}