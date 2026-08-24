package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.fluids.CTFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CTFluidTags extends FluidTagsProvider {

    CTFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CastingTools.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        TagKey<Fluid> tag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c","molten_omnithium"));
        tag(tag).add(CTFluids.MOLTEN_OMNITHIUM.getFluid());
        tag(tag).add(CTFluids.MOLTEN_OMNITHIUM.getFlowingFluid());
    }
}
