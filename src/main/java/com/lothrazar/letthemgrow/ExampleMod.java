package com.lothrazar.letthemgrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// TODO: The value here should match an entry in the META-INF/mods.toml file
// TODO: Also search and replace it in build.gradle
@Mod(ExampleMod.MODID)
public class ExampleMod {

  public static final String MODID = "letthemgrow";
  public static final Logger LOGGER = LogManager.getLogger();
  //  public static ConfigManager config;

  public ExampleMod() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    //only for server starting
    MinecraftForge.EVENT_BUS.register(this);
    //    config = new ConfigManager(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    //  config.tooltips();//config values used like this
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    World world = event.getEntity().world;
    if (!world.isRemote
        && event.getEntityLiving() instanceof AnimalEntity) {
      AnimalEntity child = (AnimalEntity) event.getEntityLiving();
      //      child.getGrowingAge();//negative means child
      int NEWBABY = -24000;
      int HALFGROWN = NEWBABY / 2;
      int QUARTERGROWN = HALFGROWN / 2;
      int THREEQGROWN = HALFGROWN + QUARTERGROWN;
      int FULLGROWN = 0;
      if (child.getGrowingAge() < FULLGROWN) {
        LOGGER.info(child.getPosition() + " == " + child.getGrowingAge());
        //it has a 50% chance of not growing
        if (world.rand.nextDouble() < 0.5) {
          //          LOGGER.info("slow down");
          child.setGrowingAge(child.getGrowingAge() - 1);
        }
        /**
         * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's positive, it get's decremented each tick. Don't confuse this with
         * EntityLiving.getAge. With a negative value the Entity is considered a child.
         */
        if (child.getGrowingAge() == -4) {}
      }
      //ageable entity LivingTick does this
      //      if (this.isAlive()) {
      //        int i = this.getGrowingAge();
      //        if (i < 0) {
      //           ++i;
      //           this.setGrowingAge(i);
      //        } else if (i > 0) {
      //           --i;
      //           this.setGrowingAge(i);
      //        } }
    }
  }

  @SubscribeEvent
  public void onServerStarting(EntityInteract event) {
    if (event.getTarget() instanceof AgeableEntity) {
      AgeableEntity growing = (AgeableEntity) event.getTarget();
      if (growing.isChild()) {
        if (growing instanceof AnimalEntity) {
          //one subclass down from ageable
          //
          //
          AnimalEntity child = (AnimalEntity) growing;
          if (child.isBreedingItem(event.getItemStack())) {
            LOGGER.info("dont feed eh");
            event.setCanceled(true);
          }
        }
      }
    }
  }
}
