package com.benbenlaw.castingtools.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ToolModifiersConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> fortuneMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> excavationMaxLevel;



    static {

        // Casting Configs
        BUILDER.comment("Casting Tools Startup Config")
                .push("Casting Tools");


        //Fortune
        fortuneMaxLevel = BUILDER.comment("The maximum level of the Fortune modifier.")
                .define("fortuneMaxLevel", 10);


        //Excavation
        excavationMaxLevel = BUILDER.comment("The maximum level of the Excavation modifier.")
                .define("excavationMaxLevel", 3);






        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();
    }
}
