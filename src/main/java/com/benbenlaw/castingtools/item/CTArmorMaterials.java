package com.benbenlaw.castingtools.item;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.utils.CTTags;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class CTArmorMaterials {

    public static final ResourceKey<EquipmentAsset> OMNITHIUM_EQUIPMENT = ResourceKey.create(EquipmentAssets.ROOT_ID, CastingTools.identifier("omnithium"));

    public static final ArmorMaterial OMNITHIUM = new ArmorMaterial(
            53,
            defense(3, 8, 7, 3, 8),
            50,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.75f,
            CTTags.Items.OMNITHIUM_INGOT,
            OMNITHIUM_EQUIPMENT
    );

    private static Map<ArmorType, Integer> defense(int helmet, int chest, int leggings, int boots, int body) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, leggings, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helmet, ArmorType.BODY, body));
    }
}
