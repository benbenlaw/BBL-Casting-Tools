package com.benbenlaw.castingtools.item;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CTItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CastingTools.MOD_ID);

    public static final DeferredItem<Item> OMNITHIUM_NUGGET = ITEMS.registerSimpleItem("omnithium_nugget");
    public static final DeferredItem<Item> OMNITHIUM_INGOT = ITEMS.registerSimpleItem("omnithium_ingot");

    public static final DeferredItem<Item> OMNITHIUM_PICKAXE = ITEMS.registerItem("omnithium_pickaxe",
            Item::new,properties -> properties
                    .pickaxe(CTToolMaterials.OMNITHIUM, 2.0F, -2.8F)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_SHOVEL = ITEMS.registerItem("omnithium_shovel",
            properties -> new ShovelItem(CTToolMaterials.OMNITHIUM, 1.5F, -3.0F,
                    properties
                    .fireResistant()
                    .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> OMNITHIUM_AXE = ITEMS.registerItem("omnithium_axe",
            properties -> new AxeItem(CTToolMaterials.OMNITHIUM, 7.0F, -3.1F,
                    properties
                    .fireResistant()
                    .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> OMNITHIUM_HOE = ITEMS.registerItem("omnithium_hoe",
        properties -> new HoeItem(CTToolMaterials.OMNITHIUM, 0.0F, -3.0F, properties
                .fireResistant()
                .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> OMNITHIUM_SWORD = ITEMS.registerItem("omnithium_sword",
            Item::new,properties -> properties
                    .sword(CTToolMaterials.OMNITHIUM, 3.0F, -2.4F)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_SPEAR = ITEMS.registerItem("omnithium_spear",
            Item::new,properties -> properties
                    .spear(CTToolMaterials.OMNITHIUM, 2.0F, 2.5F, 0.2F, 1.0F, 7.0F, 4.5F, 5.1F, 6.0F, 4.6F)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_BOW = ITEMS.registerItem("omnithium_bow",
            BowItem::new,properties -> properties
                    .durability(5000)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_CROSSBOW = ITEMS.registerItem("omnithium_crossbow",
            CrossbowItem::new,properties -> properties
                    .durability(5000)
                    .component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_HELMET = ITEMS.registerItem("omnithium_helmet",
            Item::new,properties -> properties
                    .humanoidArmor(CTArmorMaterials.OMNITHIUM, ArmorType.HELMET)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_CHESTPLATE = ITEMS.registerItem("omnithium_chestplate",
            Item::new,properties -> properties
                    .humanoidArmor(CTArmorMaterials.OMNITHIUM, ArmorType.CHESTPLATE)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_LEGGINGS = ITEMS.registerItem("omnithium_leggings",
            Item::new,properties -> properties
                    .humanoidArmor(CTArmorMaterials.OMNITHIUM, ArmorType.LEGGINGS)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_BOOTS = ITEMS.registerItem("omnithium_boots",
            Item::new,properties -> properties
                    .humanoidArmor(CTArmorMaterials.OMNITHIUM, ArmorType.BOOTS)
                    .fireResistant()
                    .rarity(Rarity.EPIC));

    public static final DeferredItem<Item> OMNITHIUM_UPGRADE_SMITHING_TEMPLATE = ITEMS.registerItem("omnithium_upgrade_smithing_template",
            OmnithiumSmithingTemplateItem::createOmnithiumUpgradeTemplate, properties -> properties
                    .rarity(Rarity.EPIC));

}
