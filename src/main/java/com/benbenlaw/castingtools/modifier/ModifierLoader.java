package com.benbenlaw.castingtools.modifier;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class ModifierLoader extends SimpleJsonResourceReloadListener<ModifierData> {

    public ModifierLoader(HolderLookup.Provider registries) {
        super(registries, ModifierData.CODEC, ModifierRegistry.MODIFIER_DATA_KEY);
    }

    @Override
    protected Map<Identifier, ModifierData> prepare(ResourceManager manager, ProfilerFiller profiler) {
        return super.prepare(manager, profiler);
    }

    @Override
    protected void apply(Map<Identifier, ModifierData> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {

        System.out.println("Loading Modifier Data...");

        for (var entry : prepared.entrySet()) {
            Identifier id = entry.getKey();
            ModifierData data = entry.getValue();

            Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
            if (modifier == null) {
                System.out.println("No modifier registered for " + id);
                continue;
            }

            modifier.setData(data);
            System.out.println("Loaded modifier data for " + id);
        }

        System.out.println("Finished loading modifiers");
    }

}