package com.lothrazar.letthemgrow;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class ConfigManagerMobgrowth {

  static final ModConfigSpec CONFIG;
  private static IntValue ANIMALCHANCE;
  private static BooleanValue DISABLEFEEDING;
  private static BooleanValue MILKNERF;
  private static IntValue CROPSCHANCE;
  private static IntValue SAPLINGCHANCE;
  static {
    final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    BUILDER.comment("General settings").push(LetThemGrowMod.MODID);
    MILKNERF = BUILDER.comment("When a cow is milked too often it becomes un-milkable for a time")
        .define("limitedMilk", true);
    SAPLINGCHANCE = BUILDER.comment("Once every tick, this is the percentage chance that growth will be blocked; so 99 is very slow, and 1 is fast.")
        .defineInRange("saplingSlowdownChance", 75, 0, 100);
    CROPSCHANCE = BUILDER.comment("Once every tick, this is the percentage chance that growth will be blocked; so 99 is very slow, and 1 is fast.")
        .defineInRange("cropsSlowdownChance", 50, 0, 100);
    ANIMALCHANCE = BUILDER.comment("Once every tick, this is the percentage chance that growth will be blocked; so 99 is very slow, and 1 is fast.")
        .defineInRange("livestockSlowdownChance", 70, 0, 100);
    DISABLEFEEDING = BUILDER.comment("True to disable feeding any compatible items to child animals").define("blockFeedingYoung", true);
    BUILDER.pop();
    CONFIG = BUILDER.build();
  }

//  public ConfigManagerMobgrowth() {
//    CONFIG.setConfig(setup(LetThemGrowMod.MODID));
//  }

  public static int getCropsChance() {
    return CROPSCHANCE.get();
  }

  public static int getAnimalChance() {
    return ANIMALCHANCE.get();
  }

  public static int getSaplingChance() {
    return SAPLINGCHANCE.get();
  }

  public static boolean disableFeeding() {
    return DISABLEFEEDING.get();
  }

  public static boolean milkNerf() {
    return MILKNERF.get();
  }
}
