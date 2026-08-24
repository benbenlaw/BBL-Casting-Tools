package com.benbenlaw.castingtools.item;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CTBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CTCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CastingTools.MOD_ID);

    public static final Supplier<CreativeModeTab> CASTING_TOOLS_TAB = CREATIVE_MODE_TABS.register("castingtools", () -> CreativeModeTab.builder()
            .withTabsBefore(Identifier.fromNamespaceAndPath(Casting.MOD_ID, "casting"))
            .icon(() -> CTBlocks.MODIFIER.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.castingtools"))
            .displayItems((featureFlagSet, output) -> {
                CTItems.ITEMS.getEntries().forEach((entry) -> output.accept(entry.get()));
            }).build());
}


