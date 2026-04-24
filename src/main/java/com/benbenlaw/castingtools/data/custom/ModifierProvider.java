package com.benbenlaw.castingtools.data.custom;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.CTTags;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModifierProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public ModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "modifier");
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return this.lookupProvider.thenCompose(registries -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            HolderGetter<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

            // Beheading
            add(cachedOutput, futures, "beheading", 1, 1, 4000)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .ingredient("#minecraft:skulls", 1)
                    .save();

            // Efficiency
            add(cachedOutput, futures, "efficiency", 8, 12, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .additionalValue(0.5)
                    .fluid("casting:molten_redstone", 1350)
                    .save();

            // Fortune
            add(cachedOutput, futures, "fortune", 7, 15, 3000)
                    .validItem(tagToString(ItemTags.PICKAXES.location()))
                    .validItem(tagToString(ItemTags.AXES.location()))
                    .validItem(tagToString(ItemTags.SHOVELS.location()))
                    .validItem(tagToString(ItemTags.HOES.location()))
                    .fluid("casting:molten_lapis", 1350)
                    .incompatibleModifier(CastingTools.identifier("silk_touch"))
                    .save();

            //Excavation
            add(cachedOutput, futures, "excavation", 3, 5, 5000)
                    .validItem(tagToString(ItemTags.PICKAXES.location()))
                    .validItem(tagToString(ItemTags.SHOVELS.location()))
                    .fluid("casting:molten_diamond", 360)
                    .incompatibleModifier(ModifierRegistry.PULVERIZING.getId())
                    .save();

            // Silk Touch
            add(cachedOutput, futures, "silk_touch", 1, 1, 4000)
                    .validItem(tagToString(ItemTags.PICKAXES.location()))
                    .validItem(tagToString(ItemTags.AXES.location()))
                    .validItem(tagToString(ItemTags.SHOVELS.location()))
                    .validItem(tagToString(ItemTags.HOES.location()))
                    .fluid("casting:molten_emerald", 720)
                    .incompatibleModifier(CastingTools.identifier("fortune"))
                    .save();

            // Looting
            add(cachedOutput, futures, "looting", 8, 15, 1500)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .ingredient("minecraft:lapis_lazuli", 16)
                    .save();

            // Torch Placer
            add(cachedOutput, futures, "torch_placer", 1, 1, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .ingredient("#c:rods/wooden", 64)
                    .fluid("casting:molten_coal", 5120)
                    .save();

            // Cobblestone Placer
            add(cachedOutput, futures, "cobblestone_placer", 1, 1,1000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .ingredient("minecraft:cobblestone", 64)
                    .fluid("casting:molten_stone", 5120)
                    .save();

            // Unbreaking
            add(cachedOutput, futures, "unbreaking", 7, 10, 4000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_WEAPONS.location()))
                    .fluid("casting:molten_obsidian", 8000)
                    .incompatibleModifier(CastingTools.identifier("repairing"))
                    .additionalValue(0.1)
                    .save();

            // Repairing
            add(cachedOutput, futures, "repairing", 5, 10, 4000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_WEAPONS.location()))
                    .ingredient("#minecraft:moss_blocks", 8)
                    .additionalValue(20)
                    .incompatibleModifier(CastingTools.identifier("unbreaking"))
                    .save();

            // Ignite
            add(cachedOutput, futures, "ignite", 5, 10, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .validItem(tagToString(Tags.Items.RODS.location()))
                    .ingredient("minecraft:flint_and_steel", 1)
                    .fluid("minecraft:lava", 8000)
                    .additionalValue(20.0)
                    .save();

            // Sharpness
            add(cachedOutput, futures, "sharpness", 8, 15, 2000)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .fluid("casting:molten_quartz", 5000)
                    .additionalValue(1.0)
                    .save();

            // Lifesteal
            add(cachedOutput, futures, "lifesteal", 7, 15, 3000)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .ingredient("minecraft:golden_apple", 4)
                    .additionalValue(0.1)
                    .save();

            // Knockback
            add(cachedOutput, futures, "knockback", 5, 10, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_MELEE_WEAPONS.location()))
                    .validItem("#c:rods")
                    .ingredient("minecraft:piston", 2)
                    .additionalValue(1.0)
                    .save();

            // Teleporting
            add(cachedOutput, futures, "teleporting", 6, 12, 5000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_WEAPONS.location()))
                    .ingredient("minecraft:ender_pearl", 16)
                    .additionalValue(8.0)
                    .save();

            // Protection
            add(cachedOutput, futures, "protection", 5, 10, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .fluid("casting:molten_steel", 720)
                    .additionalValue(0.066)
                    .save();

            // Magnet
            add(cachedOutput, futures, "magnet", 4, 8, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .ingredient("minecraft:iron_ingot", 8)
                    .fluid("casting:molten_gold", 720)
                    .save();

            // Night Vision
            add(cachedOutput, futures, "night_vision", 1, 1, 8000)
                    .validItem(tagToString(ItemTags.HEAD_ARMOR.location()))
                    .ingredient("minecraft:golden_carrot", 8)
                    .additionalValue(250.0)
                    .save();

            // Water Breathing
            add(cachedOutput, futures, "water_breathing", 1, 1,2000)
                    .validItem(tagToString(ItemTags.HEAD_ARMOR.location()))
                    .ingredient("minecraft:pufferfish", 1)
                    .additionalValue(250.0)
                    .save();

            // Speed
            add(cachedOutput, futures, "speed", 5, 8, 2500)
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .ingredient("minecraft:sugar", 8)
                    .save();

            // Sticky
            add(cachedOutput, futures, "sticky", 5, 10, 1000)
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .ingredient("minecraft:slime_ball", 8)
                    .additionalValue(0.5)
                    .save();

            // Flight
            add(cachedOutput, futures, "flight", 1, 1, 16000)
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .ingredient("minecraft:nether_star", 4)
                    .fluid("casting:molten_netherite", 1620)
                    .save();

            // Soulbound
            add(cachedOutput, futures, "soulbound", 1, 1, 8000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .validItem(tagToString(CTTags.Items.ALL_WEAPONS.location()))
                    .ingredient("minecraft:heart_of_the_sea", 1)
                    .save();

            // Bouncy
            add(cachedOutput, futures, "bouncy", 1, 1, 1000)
                    .validItem(tagToString(ItemTags.FOOT_ARMOR.location()))
                    .ingredient("minecraft:slime_block", 4)
                    .additionalValue(1.30)
                    .save();

            // Retaliation
            add(cachedOutput, futures, "retaliation", 5, 10, 8000)
                    .validItem(tagToString(CTTags.Items.ALL_ARMORS.location()))
                    .ingredient("minecraft:diamond", 6)
                    .additionalValue(1.0)
                    .save();

            // Pulverizing
            add(cachedOutput, futures, "pulverizing", 1, 1, 4000)
                    .validItem(tagToString(CTTags.Items.ALL_TOOLS.location()))
                    .ingredient("minecraft:gravel", 64)
                    .incompatibleModifier(ModifierRegistry.EXCAVATION.getId())
                    .save();

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private ModifierBuilder add(CachedOutput cache, List<CompletableFuture<?>> futures, String id, int maxLevel, int maxEnhancedLevel, int cost) {
        return ModifierBuilder.create(maxLevel, maxEnhancedLevel, cost, nameTranslationKey(id), descriptionTranslationKey(id))
                .setSaveCallback(builder -> {
                    ModifierData data = builder.build();
                    saveModifier(cache, futures, CastingTools.identifier(id), data);
                });
    }


    private void saveModifier(CachedOutput cache, List<CompletableFuture<?>> futures, Identifier id, ModifierData data) {
        Path path = this.pathProvider.json(id);
        var json = ModifierData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow();
        futures.add(DataProvider.saveStable(cache, json, path));
    }

    public String nameTranslationKey(String id) { return "modifier." + CastingTools.MOD_ID + "." + id; }
    public String descriptionTranslationKey(String id) { return "modifier." + CastingTools.MOD_ID + "." + id + ".description"; }
    public String tagToString(Identifier tag) { return "#" + tag.toString(); }

    @Override
    public String getName() { return CastingTools.MOD_ID + " Modifiers"; }
}