package com.benbenlaw.castingtools.modifier;

import com.benbenlaw.castingtools.CastingTools;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;
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