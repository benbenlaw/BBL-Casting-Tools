package com.benbenlaw.castingtools.item;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CastingToolsDataComponent {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, CastingTools.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ModifierComponent>> MODIFIER_COMPONENT =
            COMPONENTS.register("modifier_component", () ->
                    DataComponentType.<ModifierComponent>builder()
                            .persistent(ModifierComponent.CODEC)
                            .networkSynchronized(ModifierComponent.STREAM_CODEC)
                            .cacheEncoding()
                            .build());
}
