package com.benbenlaw.castingtools.utils;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.core.util.CoreTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CTTags {

    public static class Items extends CoreTags.Items {

        //Items

        public static final TagKey<Item> ENHANCED = tag(CastingTools.MOD_ID, "is_enhanceable");
        public static final TagKey<Item> NOT_MODIFIABLE = tag(CastingTools.MOD_ID, "not_modifiable");

        public static final TagKey<Item> ALL_TOOLS = tag(CastingTools.MOD_ID, "all_tools");
        public static final TagKey<Item> ALL_ARMORS = tag(CastingTools.MOD_ID, "all_armors");

        public static final TagKey<Item> ALL_WEAPONS = tag(CastingTools.MOD_ID, "all_weapons");
        public static final TagKey<Item> ALL_MELEE_WEAPONS = tag(CastingTools.MOD_ID, "all_melee_weapons");
        public static final TagKey<Item> ALL_RANGED_WEAPONS = tag(CastingTools.MOD_ID, "all_ranged_weapons");

        public static final TagKey<Item> OMNITHIUM_NUGGET = tag("c", "nuggets/omnithium");
        public static final TagKey<Item> OMNITHIUM_INGOT = tag("c", "ingots/omnithium");
        public static final TagKey<Item> OMNITHIUM_BLOCK = tag("c", "storage_blocks/omnithium");

    }

    public static class Blocks extends CoreTags.Blocks {
        public static final TagKey<Block> OMNITHIUM_BLOCK = tag("c", "storage_blocks/omnithium");
    }
}
