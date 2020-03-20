package com.lothrazar.letthemgrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(GrowthMod.MODID)
public class GrowthMod {

  public static final String MODID = "letthemgrow";
  public static final Logger LOGGER = LogManager.getLogger();
  public static ConfigManager config;

  public GrowthMod() {
    MinecraftForge.EVENT_BUS.register(new GrowthEvents());
    config = new ConfigManager(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }
}
