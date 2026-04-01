package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CastingToolsLangProvider extends LanguageProvider {

    public CastingToolsLangProvider(PackOutput output) {
        super(output, CastingTools.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.castingmb", "Casting - Multiblocks");

        //Blocks
        add("block.castingtools.modifier", "Modifier");

        //Misc
        add("modifier.castingtools.incompatible", "Incompatible With");
        add("modifier.castingtools.max_level", "Max Level %s");

        //JEI
        add("jei.castingtools.modifier_applying", "Applying %s Modifier");

        //Modifiers
        add("modifier.castingtools.excavation", "Excavation");
        add("modifier.castingtools.silk_touch", "Silk Touch");
        add("modifier.castingtools.fortune", "Fortune");
        add("modifier.castingtools.sharpness", "Sharpness");
        add("modifier.castingtools.ignite", "Ignite");

        //Modifier Descriptions
        add("modifier.castingtools.silk_touch.description", "Allows you to mine blocks and receive the block itself instead of its usual drops, such as mining stone and receiving a stone block instead of cobblestone");
        add("modifier.castingtools.excavation.description", "Increases the area of effect when mining, allowing you to mine multiple blocks at once");
        add("modifier.castingtools.fortune.description", "Increases the chance of receiving more items when mining certain block, like Ores");
        add("modifier.castingtools.sharpness.description", "Increases the damage dealt to mobs");
        add("modifier.castingtools.ignite.description", "Sets mobs on fire when hit");
    }
}
