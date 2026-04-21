package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
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

        //Armor
        tag(CastingToolsTags.Items.ALL_ARMORS)
                .addTag(ItemTags.HEAD_ARMOR)
                .addTag(ItemTags.CHEST_ARMOR)
                .addTag(ItemTags.LEG_ARMOR)
                .addTag(ItemTags.FOOT_ARMOR)
        ;

        //All Tools
        tag(CastingToolsTags.Items.ALL_TOOLS)
                .addTag(ItemTags.PICKAXES)
                .addTag(ItemTags.AXES)
                .addTag(ItemTags.SHOVELS)
                .addTag(ItemTags.HOES)
                .addTag(ItemTags.SWORDS)
                .addTag(Tags.Items.TOOLS_SHEAR)
        ;

        //All Weapons
        tag(CastingToolsTags.Items.ALL_WEAPONS)
                .addTag(CastingToolsTags.Items.ALL_MELEE_WEAPONS)
                .addTag(CastingToolsTags.Items.ALL_RANGED_WEAPONS)
        ;

        //Melee Weapons
        tag(CastingToolsTags.Items.ALL_MELEE_WEAPONS)
                .addTag(ItemTags.SWORDS)
                .addTag(ItemTags.AXES)
                .addTag(Tags.Items.TOOLS_MACE)
        ;

        //Ranged Weapons
        tag(CastingToolsTags.Items.ALL_RANGED_WEAPONS)
                .addTag(Tags.Items.TOOLS_BOW)
                .addTag(Tags.Items.TOOLS_CROSSBOW)
        ;


    }
}
