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
        add("itemGroup.castingtools", "Casting - Tools");

        //Blocks
        add("block.castingtools.modifier", "Modifier");

        //Misc
        add("modifier.castingtools.incompatible", "Incompatible With");
        add("modifier.castingtools.max_level", "Max Level %s");

        //JEI
        add("jei.castingtools.modifier", "Modifier");
        add("jei.castingtools.modifier.information", "The Modifier can be used to make your tools and equipment more powerful.\n\nThe modifier uses Molten Experience as a fuel as well as either another fluid, an item or a Fluid and Item to apply a modifier.\n\nStanding on the Modifier will consume your experience and insert into the internal experience tank");

        //Modifiers
        add("modifier.castingtools.excavation", "Excavation");
        add("modifier.castingtools.silk_touch", "Silk Touch");
        add("modifier.castingtools.fortune", "Fortune");
        add("modifier.castingtools.sharpness", "Sharpness");
        add("modifier.castingtools.ignite", "Ignite");
        add("modifier.castingtools.efficiency", "Efficiency");
        add("modifier.castingtools.unbreaking", "Unbreaking");
        add("modifier.castingtools.repairing", "Repairing");
        add("modifier.castingtools.beheading", "Beheading");
        add("modifier.castingtools.torch_placer", "Torch Placer");
        add("modifier.castingtools.looting", "Looting");
        add("modifier.castingtools.lifesteal", "Lifesteal");
        add("modifier.castingtools.knockback", "Knockback");
        add("modifier.castingtools.teleporting", "Teleporting");
        add("modifier.castingtools.protection", "Protection");
        add("modifier.castingtools.soulbound", "Soulbound");
        add("modifier.castingtools.magnet", "Magnet");
        add("modifier.castingtools.night_vision", "Night Vision");
        add("modifier.castingtools.water_breathing", "Water Breathing");
        add("modifier.castingtools.speed", "Speed");
        add("modifier.castingtools.sticky", "Sticky");
        add("modifier.castingtools.flight", "Flight");
        add("modifier.castingtools.bouncy", "Bouncy");


        //Modifier Descriptions
        add("modifier.castingtools.silk_touch.description", "Allows you to mine blocks and receive the block itself instead of its usual drops, such as mining stone and receiving a stone block instead of cobblestone");
        add("modifier.castingtools.excavation.description", "Increases the area of effect when mining, allowing you to mine multiple blocks at once");
        add("modifier.castingtools.fortune.description", "Increases the chance of receiving more items when mining certain block, like Ores");
        add("modifier.castingtools.sharpness.description", "Increases the damage dealt to mobs");
        add("modifier.castingtools.ignite.description", "Sets mobs on fire when hit");
        add("modifier.castingtools.efficiency.description", "Increases mining speed");
        add("modifier.castingtools.unbreaking.description", "Reduces the chance of you tool taking damage when used");
        add("modifier.castingtools.repairing.description", "Repairs your tools and equipment over time");
        add("modifier.castingtools.beheading.description", "Valid mobs will always drop their head when killed");
        add("modifier.castingtools.torch_placer.description", "Places a torch when you right click. Doesn't require torches in your inventory");
        add("modifier.castingtools.looting.description", "Increases the chance of mobs dropping more items when killed");
        add("modifier.castingtools.lifesteal.description", "Heals you for a portion of the damage dealt to mobs");
        add("modifier.castingtools.knockback.description", "Increases the knockback to mobs when hit");
        add("modifier.castingtools.teleporting.description", "Teleports you to a random location within a 5 block radius when you right click. Doesn't require any items in your inventory");
        add("modifier.castingtools.protection.description", "Reduces incoming damage from mobs and other sources");
        add("modifier.castingtools.magnet.description", "Attracts nearby items to you");
        add("modifier.castingtools.night_vision.description", "Provides you with night vision");
        add("modifier.castingtools.water_breathing.description", "Allows you to breathe underwater");
        add("modifier.castingtools.speed.description", "Increases your movement speed");
        add("modifier.castingtools.sticky.description", "Stick to walls just like a spider");
        add("modifier.castingtools.flight.description", "Provides flight");
        add("modifier.castingtools.bouncy.description", "Bounce when you fall from heights");





    }
}
