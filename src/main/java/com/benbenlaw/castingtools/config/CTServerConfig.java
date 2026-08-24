package com.benbenlaw.castingtools.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CTServerConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> costForUpgrader;


    static {

        // Casting Configs
        BUILDER.comment("Casting Tools Startup Config")
                .push("Casting Tools");


        //Fire Aspect
        costForUpgrader = BUILDER.comment("The cost of experience to use the Upgrader block.")
                .define("costForUpgrader", 16000);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();
    }
}
