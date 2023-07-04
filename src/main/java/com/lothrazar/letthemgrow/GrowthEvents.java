package com.lothrazar.letthemgrow;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowthEvents {

  private static final String MILKED_NBTKEY = LetThemGrowMod.MODID + ":milked";
  static final int FULLGROWN = 0;

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingTickEvent event) {
    Level world = event.getEntity().level();
    if (!world.isClientSide
        && event.getEntity() instanceof Animal child) {
      //      Animal child = (Animal) event.getEntityLiving();
      if (child.getAge() < FULLGROWN) {
        //it has a 50% chance of not growing
        if (world.random.nextDouble() * 100 < ConfigManagerMobgrowth.getAnimalChance()) {
          child.setAge(child.getAge() - 1);
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
    Player player = event.getEntity();
    if (ConfigManagerMobgrowth.milkNerf()
        && !player.level().isClientSide
        && !player.isCreative()
        && event.getTarget() instanceof Cow
        && event.getItemStack().getItem() == Items.BUCKET) {
      Cow cow = (Cow) event.getTarget();
      if (!cow.isBaby()) {
        //chance of turning back into child 
        //is crazy and makes no sense but makes it un-milkeable
        //non random is better,can we keep track of minimum 
        //even if nbt gets wiped
        int prev = cow.getPersistentData().getInt(MILKED_NBTKEY);
        if (prev >= 6
            && player.level().random.nextDouble() < 0.25) {
          //after a few freebies, then there is a chance the bad thing happens
          cow.setAge(-24000);
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
    if (event.getLevel().getRandom().nextDouble() * 100 <= ConfigManagerMobgrowth.getCropsChance()) {
      event.setResult(Result.DENY);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    if (event.getLevel().getRandom().nextDouble() * 100 <= ConfigManagerMobgrowth.getSaplingChance()) {
      event.setResult(Result.DENY);
    }
  }

  @SubscribeEvent
  public void onEntityInteract(EntityInteract event) {
    if (ConfigManagerMobgrowth.disableFeeding() && event.getTarget() instanceof AgeableMob growing) {
      if (growing.isBaby()) {
        if (growing instanceof Animal child) {
          //one subclass down from ageable 
          if (child.isFood(event.getItemStack())) {
            event.setCanceled(true);
          }
        }
      }
    }
  }
}
