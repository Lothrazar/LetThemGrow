package com.lothrazar.letthemgrow;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowthEvents {

  private static final String MILKED_NBTKEY = GrowthMod.MODID + ":milked";

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    World world = event.getEntity().world;
    if (!world.isRemote
        && event.getEntityLiving() instanceof AnimalEntity) {
      AnimalEntity child = (AnimalEntity) event.getEntityLiving();
      int FULLGROWN = 0;
      if (child.getGrowingAge() < FULLGROWN) {
        //it has a 50% chance of not growing
        if (world.rand.nextDouble() * 100 < GrowthMod.config.getAnimalChance()) {
          child.setGrowingAge(child.getGrowingAge() - 1);
        }
      }
      /**
       * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's positive, it get's decremented each tick. Don't confuse this with
       * EntityLiving.getAge. With a negative value the Entity is considered a child.
       */
    }
  }

  @SubscribeEvent
  public void onEntity(PlayerInteractEvent.EntityInteract event) {
    //milking timer
    PlayerEntity player = event.getPlayer();
    if (GrowthMod.config.milkNerf()
        && !player.world.isRemote
        && !player.abilities.isCreativeMode
        && event.getTarget() instanceof CowEntity
        && event.getItemStack().getItem() == Items.BUCKET) {
      CowEntity cow = (CowEntity) event.getTarget();
      if (!cow.isChild()) {
        //chance of turning back into child 
        //is crazy and makes no sense but makes it un-milkeable
        //non random is better,can we keep track of minimum 
        //even if nbt gets wiped
        int prev = cow.getPersistentData().getInt(MILKED_NBTKEY);
        if (prev >= 6
            && player.world.rand.nextDouble() < 0.25) {
          //after a few freebies, then there is a chance the bad thing happens
          cow.setGrowingAge(-24000);
          cow.getPersistentData().putInt(MILKED_NBTKEY, 0);
          cow.getPersistentData().remove(MILKED_NBTKEY);
        }
        cow.getPersistentData().putInt(MILKED_NBTKEY, prev + 1);
      }
    }
  }

  //swords pass thru them unharmned? 
  /**
   * Fired when any "growing age" blocks (for example cacti, chorus plants, or crops in vanilla) attempt to advance to the next growth age state during a random tick.<br>
   * <br>
   * {@link Result#DEFAULT} will pass on to the vanilla growth mechanics.<br>
   * {@link Result#ALLOW} will force the plant to advance a growth stage.<br>
   * {@link Result#DENY} will prevent the plant from advancing a growth stage.<br>
   * <br>
   * This event is not {@link Cancelable}.<br>
   * <br>
   */
  @SubscribeEvent
  public void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
    if (event.getWorld().getRandom().nextDouble() * 100 <= GrowthMod.config.getCropsChance()) {
      //      GrowthMod.LOGGER.info(event.getResult() +
      //          "" + event.getState() + " cancel" + event.getPos());
      event.setResult(Result.DENY);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    if (event.getWorld().getRandom().nextDouble() * 100 <= GrowthMod.config.getSaplingChance()) {
      //      GrowthMod.LOGGER.info(event.getResult() +
      //          " sapling cancel " + event.getPos());
      event.setResult(Result.DENY);
    }
  }

  @SubscribeEvent
  public void onServerStarting(EntityInteract event) {
    if (GrowthMod.config.disableFeeding()
        && event.getTarget() instanceof AgeableEntity) {
      AgeableEntity growing = (AgeableEntity) event.getTarget();
      if (growing.isChild()) {
        if (growing instanceof AnimalEntity) {
          //one subclass down from ageable
          AnimalEntity child = (AnimalEntity) growing;
          if (child.isBreedingItem(event.getItemStack())) {
            event.setCanceled(true);
          }
        }
      }
    }
  }
}
