package com.lothrazar.letthemgrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(AnimalGrowthMod.MODID)
public class AnimalGrowthMod {

  public static final String MODID = "letthemgrow";
  public static final Logger LOGGER = LogManager.getLogger();
  public static ConfigManager config;

  public AnimalGrowthMod() {
    MinecraftForge.EVENT_BUS.register(new GrowthEvents());
    config = new ConfigManager(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }
}
