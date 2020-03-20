package com.lothrazar.letthemgrow;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowthEvents {

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    World world = event.getEntity().world;
    if (!world.isRemote
        && event.getEntityLiving() instanceof AnimalEntity) {
      AnimalEntity child = (AnimalEntity) event.getEntityLiving();
      // int NEWBABY = -24000;
      int FULLGROWN = 0;
      if (child.getGrowingAge() < FULLGROWN) {
        //it has a 50% chance of not growing
        if (world.rand.nextDouble() * 100 < AnimalGrowthMod.config.chanceBlock()) {
          AnimalGrowthMod.LOGGER.info("slow down");
          child.setGrowingAge(child.getGrowingAge() - 1);
        }
      }
      /**
       * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's positive, it get's decremented each tick. Don't confuse this with
       * EntityLiving.getAge. With a negative value the Entity is considered a child.
       */
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
    if (AnimalGrowthMod.config.disableFeeding()
        && event.getTarget() instanceof AgeableEntity) {
      AgeableEntity growing = (AgeableEntity) event.getTarget();
      if (growing.isChild()) {
        if (growing instanceof AnimalEntity) {
          //one subclass down from ageable
          //
          //
          AnimalEntity child = (AnimalEntity) growing;
          if (child.isBreedingItem(event.getItemStack())) {
            AnimalGrowthMod.LOGGER.info("dont feed eh");
            event.setCanceled(true);
          }
        }
      }
    }
  }
}
