package com.lothrazar.letthemgrow;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(LetThemGrowMod.MODID)
public class LetThemGrowMod {

  public static final String MODID = "letthemgrow";
  public static final Logger LOGGER = LogUtils.getLogger();

  public LetThemGrowMod(IEventBus modEventBus, ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.COMMON, ConfigManagerMobgrowth.CONFIG);
    NeoForge.EVENT_BUS.register(new GrowthEvents());
  }
}
