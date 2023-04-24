package com.lothrazar.letthemgrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(GrowthMod.MODID)
public class GrowthMod {

  public static final String MODID = "letthemgrow";
  public static final Logger LOGGER = LogManager.getLogger();
  public static ConfigManagerMobgrowth CONFIG;

  public GrowthMod() {
    new ConfigManagerMobgrowth();
    MinecraftForge.EVENT_BUS.register(new GrowthEvents());
  }
}
