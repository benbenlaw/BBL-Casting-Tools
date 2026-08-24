package com.benbenlaw.castingtools.screen;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CTMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, CastingTools.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ModifierMenu>> MODIFIER_MENU =
            MENUS.register("modifier_menu", () -> IMenuTypeExtension.create(ModifierMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<UpgraderMenu>> UPGRADER_MENU =
            MENUS.register("upgrader_menu", () -> IMenuTypeExtension.create(UpgraderMenu::new));
}