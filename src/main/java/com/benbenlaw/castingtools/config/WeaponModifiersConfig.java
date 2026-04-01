package com.benbenlaw.castingtools.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class WeaponModifiersConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> igniteMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> igniteDurationPerLevel;

    public static final ModConfigSpec.ConfigValue<Integer> sharpnessMaxLevel;
    public static final ModConfigSpec.ConfigValue<Float> sharpnessDamagePerLevel;


    static {

        // Casting Configs
        BUILDER.comment("Casting Tools Startup Config")
                .push("Casting Tools");


        //Fire Aspect
        igniteMaxLevel = BUILDER.comment("The maximum level of the Fire Aspect modifier.")
                .define("fireAspectMaxLevel", 5);
        igniteDurationPerLevel = BUILDER.comment("The duration of the fire effect per level of the Fire Aspect modifier, in seconds.")
                .define("fireAspectDurationPerLevel", 20);

        //Sharpness
        sharpnessMaxLevel = BUILDER.comment("The maximum level of the Sharpness modifier.")
                .define("sharpnessMaxLevel", 10);
        sharpnessDamagePerLevel = BUILDER.comment("The additional damage per level of the Sharpness modifier.")
                .define("sharpnessDamagePerLevel", 1.0f);




        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();
    }
}
