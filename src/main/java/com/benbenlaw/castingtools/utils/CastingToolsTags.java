package com.benbenlaw.castingtools.utils;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.util.CoreTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CastingToolsTags {

    public static class Items extends CoreTags.Items {

        //Items
        public static final TagKey<Item> NOT_MODIFIABLE = tag(CastingTools.MOD_ID, "not_modifiable");

        public static final TagKey<Item> ALL_TOOLS = tag(CastingTools.MOD_ID, "all_tools");
        public static final TagKey<Item> ALL_ARMORS = tag(CastingTools.MOD_ID, "all_armors");

        public static final TagKey<Item> ALL_WEAPONS = tag(CastingTools.MOD_ID, "all_weapons");
        public static final TagKey<Item> ALL_MELEE_WEAPONS = tag(CastingTools.MOD_ID, "all_melee_weapons");
        public static final TagKey<Item> ALL_RANGED_WEAPONS = tag(CastingTools.MOD_ID, "all_ranged_weapons");

    }
}
