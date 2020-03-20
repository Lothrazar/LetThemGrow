package com.lothrazar.letthemgrow;

import java.nio.file.Path;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  private static IntValue ANIMALCHANCE;
  private static BooleanValue DISABLEFEEDING;
  private static IntValue CROPSCHANCE;
  static {
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("General settings").push(GrowthMod.MODID);
    CROPSCHANCE = COMMON_BUILDER.comment("Once every tick, this is the percentage chance that growth will be blocked; so 99 is very slow, and 1 is fast.")
        .defineInRange("cropsSlowdownChance", 75, 1, 99);
    ANIMALCHANCE = COMMON_BUILDER.comment("Once every tick, this is the percentage chance that growth will be blocked; so 99 is very slow, and 1 is fast.")
        .defineInRange("livestockSlowdownChance", 75, 1, 99);
    DISABLEFEEDING = COMMON_BUILDER.comment("True to disable feeding any compatible items to child animals").define("blockFeedingYoung", true);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public ConfigManager(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public int getCropsChance() {
    return CROPSCHANCE.get();
  }

  public int getAnimalChance() {
    return ANIMALCHANCE.get();
  }

  public boolean disableFeeding() {
    return DISABLEFEEDING.get();
  }
}
