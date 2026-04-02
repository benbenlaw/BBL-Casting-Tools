package com.benbenlaw.castingtools.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ToolModifiersConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> fortuneMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> excavationMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> efficiencyMaxLevel;
    public static final ModConfigSpec.ConfigValue<Float> efficiencyMiningSpeedPerLevel;
    public static final ModConfigSpec.ConfigValue<Integer> unbreakingMaxLevel;
    public static final ModConfigSpec.ConfigValue<Float> unbreakingChancePerLevel;
    public static final ModConfigSpec.ConfigValue<Integer> repairingMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> repairingTickReductionMaxLevel;
    public static final ModConfigSpec.ConfigValue<Integer> repairingBaseTickAtFirstLevel;
    public static final ModConfigSpec.ConfigValue<Integer> lootingMaxLevel;







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

        //Efficiency
        efficiencyMaxLevel = BUILDER.comment("The maximum level of the Efficiency modifier.")
                .define("efficiencyMaxLevel", 10);
        efficiencyMiningSpeedPerLevel = BUILDER.comment("The amount of mining speed added per level of the Efficiency modifier.")
                .define("efficiencyMiningSpeedPerLevel", 0.5f);

        //Unbreaking
        unbreakingMaxLevel = BUILDER.comment("The maximum level of the Unbreaking modifier.")
                .define("unbreakingMaxLevel", 10);
        unbreakingChancePerLevel = BUILDER.comment("The chance to not consume durability per level of the Unbreaking modifier.")
                .define("unbreakingChancePerLevel", 0.1f);

        //Repairing
        repairingMaxLevel = BUILDER.comment("The maximum level of the Repairing modifier.")
                .define("repairingMaxLevel", 8);
        repairingTickReductionMaxLevel = BUILDER.comment("The maximum level of the Repairing Tick Reduction modifier.")
                .define("repairingTickReductionMaxLevel", 20);
        repairingBaseTickAtFirstLevel = BUILDER.comment("The base amount of ticks it takes to repair at the first level of the Repairing modifier.")
                .define("repairingBaseTickAtFirstLevel", 200);

        //Looting
        lootingMaxLevel = BUILDER.comment("The maximum level of the Looting modifier.")
                .define("lootingMaxLevel", 10);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();
    }
}
