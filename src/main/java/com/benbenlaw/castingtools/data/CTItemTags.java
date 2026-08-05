package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.fluids.CTFluids;
import com.benbenlaw.castingtools.item.CastingToolsItems;
import com.benbenlaw.castingtools.utils.CTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CTItemTags extends ItemTagsProvider {

    public CTItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CastingTools.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Tools
        tag(ItemTags.PICKAXES).add(CastingToolsItems.OMNITHIUM_PICKAXE.get());
        tag(ItemTags.SHOVELS).add(CastingToolsItems.OMNITHIUM_SHOVEL.get());
        tag(ItemTags.AXES).add(CastingToolsItems.OMNITHIUM_AXE.get());
        tag(ItemTags.HOES).add(CastingToolsItems.OMNITHIUM_HOE.get());
        tag(ItemTags.SWORDS).add(CastingToolsItems.OMNITHIUM_SWORD.get());
        tag(ItemTags.SPEARS).add(CastingToolsItems.OMNITHIUM_SPEAR.get());
        tag(ItemTags.HEAD_ARMOR).add(CastingToolsItems.OMNITHIUM_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(CastingToolsItems.OMNITHIUM_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(CastingToolsItems.OMNITHIUM_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(CastingToolsItems.OMNITHIUM_BOOTS.get());
        tag(Tags.Items.TOOLS_BOW).add(CastingToolsItems.OMNITHIUM_BOW.get());
        tag(Tags.Items.TOOLS_CROSSBOW).add(CastingToolsItems.OMNITHIUM_CROSSBOW.get());

        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(CastingToolsItems.OMNITHIUM_LEGGINGS.get())
                .add(CastingToolsItems.OMNITHIUM_CHESTPLATE.get())
                .add(CastingToolsItems.OMNITHIUM_HELMET.get())
                .add(CastingToolsItems.OMNITHIUM_BOOTS.get())
                ;

        //Ingot
        tag(Tags.Items.INGOTS).add(CastingToolsItems.OMNITHIUM_INGOT.get());
        tag(CTTags.Items.OMNITHIUM_INGOT).add(CastingToolsItems.OMNITHIUM_INGOT.get());

        //Nuggets
        tag(Tags.Items.NUGGETS).add(CastingToolsItems.OMNITHIUM_NUGGET.get());
        tag(CTTags.Items.OMNITHIUM_NUGGET).add(CastingToolsItems.OMNITHIUM_NUGGET.get());

        //Blocks
        tag(Tags.Items.STORAGE_BLOCKS).add(CastingToolsBlocks.OMNITHIUM_BLOCK.get().asItem());
        tag(CTTags.Items.OMNITHIUM_BLOCK).add(CastingToolsBlocks.OMNITHIUM_BLOCK.get().asItem());

        //Bucket
        tag(Tags.Items.BUCKETS).add(CTFluids.MOLTEN_OMNITHIUM.getBucket());

        //Is Enhanced
        tag(CTTags.Items.ENHANCED)
                .add(CastingToolsItems.OMNITHIUM_PICKAXE.get())
                .add(CastingToolsItems.OMNITHIUM_SHOVEL.get())
                .add(CastingToolsItems.OMNITHIUM_AXE.get())
                .add(CastingToolsItems.OMNITHIUM_HOE.get())
                .add(CastingToolsItems.OMNITHIUM_SWORD.get())
                .add(CastingToolsItems.OMNITHIUM_SPEAR.get())
                .add(CastingToolsItems.OMNITHIUM_HELMET.get())
                .add(CastingToolsItems.OMNITHIUM_CHESTPLATE.get())
                .add(CastingToolsItems.OMNITHIUM_LEGGINGS.get())
                .add(CastingToolsItems.OMNITHIUM_BOOTS.get())
                .add(CastingToolsItems.OMNITHIUM_BOW.get())
                .add(CastingToolsItems.OMNITHIUM_CROSSBOW.get())
        ;


        //Armor
        tag(CTTags.Items.ALL_ARMORS)
                .addTag(ItemTags.HEAD_ARMOR)
                .addTag(ItemTags.CHEST_ARMOR)
                .addTag(ItemTags.LEG_ARMOR)
                .addTag(ItemTags.FOOT_ARMOR)
        ;

        //All Tools
        tag(CTTags.Items.ALL_TOOLS)
                .addTag(ItemTags.PICKAXES)
                .addTag(ItemTags.AXES)
                .addTag(ItemTags.SHOVELS)
                .addTag(ItemTags.HOES)
                .addTag(ItemTags.SWORDS)
                .addTag(Tags.Items.TOOLS_SHEAR)
        ;

        //All Weapons
        tag(CTTags.Items.ALL_WEAPONS)
                .addTag(CTTags.Items.ALL_MELEE_WEAPONS)
                .addTag(CTTags.Items.ALL_RANGED_WEAPONS)
        ;

        //Melee Weapons
        tag(CTTags.Items.ALL_MELEE_WEAPONS)
                .addTag(ItemTags.SWORDS)
                .addTag(ItemTags.AXES)
                .addTag(ItemTags.SPEARS)
                .addTag(Tags.Items.TOOLS_MACE)
        ;

        //Ranged Weapons
        tag(CTTags.Items.ALL_RANGED_WEAPONS)
                .addTag(Tags.Items.TOOLS_BOW)
                .addTag(Tags.Items.TOOLS_CROSSBOW)
        ;

        //


    }
}
