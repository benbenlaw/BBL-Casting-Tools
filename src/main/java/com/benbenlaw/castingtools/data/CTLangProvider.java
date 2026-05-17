package com.benbenlaw.castingtools.data;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CTLangProvider extends LanguageProvider {

    public CTLangProvider(PackOutput output) {
        super(output, CastingTools.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.castingtools", "Casting - Tools");

        //Blocks
        add("block.castingtools.modifier", "Modifier");
        add("block.castingtools.omnithium_block", "Omnithium Block");
        add("block.castingtools.molten_omnithium", "Molten Omnithium");

        //Items
        add("item.castingtools.omnithium_nugget", "Omnithium Nugget");
        add("item.castingtools.omnithium_ingot", "Omnithium Ingot");
        add("item.castingtools.omnithium_pickaxe", "Omnithium Pickaxe");
        add("item.castingtools.omnithium_axe", "Omnithium Axe");
        add("item.castingtools.omnithium_shovel", "Omnithium Shovel");
        add("item.castingtools.omnithium_sword", "Omnithium Sword");
        add("item.castingtools.omnithium_hoe", "Omnithium Hoe");
        add("item.castingtools.omnithium_spear", "Omnithium Spear");
        add("item.castingtools.omnithium_helmet", "Omnithium Helmet");
        add("item.castingtools.omnithium_chestplate", "Omnithium Chestplate");
        add("item.castingtools.omnithium_leggings", "Omnithium Leggings");
        add("item.castingtools.omnithium_boots", "Omnithium Boots");

        add("item.castingtools.molten_omnithium_bucket", "Omnithium Bucket");

        //Fluids
        add("fluid.castingtools.molten_omnithium", "Molten Omnithium");

        //Misc
        add("modifier.castingtools.incompatible", "Incompatible With");
        add("modifier.castingtools.max_level", "Max Level %s");
        add("modifier.castingtools.max_enhanced_level", "Max Enhanced Level %s");

        //JEI
        add("jei.castingtools.modifier", "Modifier");
        add("jei.castingtools.pulverizing", "Pulverizing");
        add("jei.castingtools.treasure", "Treasure!");
        add("jei.castingtools.beheading", "Beheading!");
        add("tooltip.castingtools.beheading", "Drops from %s when tool has beheading modifier");
        add("tooltip.castingtools.treasure", "Breaking %s when tool has the treasure modifier");
        add("tooltip.castingtools.pulverizing", "Breaking %s when tool has the pulverizing modifier");
        add("jei.castingtools.chance", "Chance: %s%%");
        add("jei.castingtools.modifier.information", "The Modifier can be used to make your tools and equipment more powerful.\n\nThe modifier uses Molten Experience as a fuel as well as either another fluid, an item or a Fluid and Item to apply a modifier.\n\nStanding on the Modifier will consume your experience and insert into the internal experience tank");

        //Modifiers
        ModifierRegistry.MODIFIER_REGISTRY.entrySet().forEach(entry -> {
            Identifier id = entry.getKey().identifier();
            String key = "modifier.castingtools." + id.getPath();
            String name = formatName(id.getPath());
            add(key, name);
        });

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
        add("modifier.castingtools.soulbound.description", "Items are returned to you upon death instead of being dropped on the ground");
        add("modifier.castingtools.magnet.description", "Attracts nearby items to you");
        add("modifier.castingtools.night_vision.description", "Provides you with night vision");
        add("modifier.castingtools.water_breathing.description", "Allows you to breathe underwater");
        add("modifier.castingtools.speed.description", "Increases your movement speed");
        add("modifier.castingtools.sticky.description", "Stick to walls just like a spider");
        add("modifier.castingtools.flight.description", "Provides flight");
        add("modifier.castingtools.bouncy.description", "Bounce when you fall from heights");
        add("modifier.castingtools.cobblestone_placer.description", "Places a block of cobblestone when you right click. Doesn't require any items in your inventory");
        add("modifier.castingtools.retaliation.description", "Sometimes damages the attacker when hit. The higher the level, the higher the chance and damage");
        add("modifier.castingtools.pulverizing.description", "Crushes up certain blocks when broken");
        add("modifier.castingtools.treasure.description", "Find additional drops from certain blocks when broken");
        add("modifier.castingtools.lightning_strike.description", "Summons a lightning bolt when you hit a mob dealing additional damage");

    }

    private static String formatName(String path) {

        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
        }

        return sb.toString().trim();
    }
}
