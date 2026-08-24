package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTArmorMaterials;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.function.BiConsumer;

public class CTEquipmentAssetProvider extends EquipmentAssetProvider {
    public CTEquipmentAssetProvider(PackOutput output) {
        super(output);
    }
    
    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(
                CTArmorMaterials.OMNITHIUM_EQUIPMENT,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(CastingTools.identifier("omnithium"))
                        .build()
        );
    }
}