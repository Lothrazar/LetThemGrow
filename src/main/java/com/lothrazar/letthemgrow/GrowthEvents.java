package com.lothrazar.letthemgrow;

import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class GrowthEvents {

  private static final String MILKED_NBTKEY = LetThemGrowMod.MODID + ":milked";
  static final int FULLGROWN = 0;

  @SubscribeEvent
  public void onLivingUpdateEvent(EntityTickEvent.Post event) {
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
  // not ICancellableEvent , use new pre result enum
  @SubscribeEvent
  public void onCropGrow(CropGrowEvent.Pre event) {
    if (event.getLevel().getRandom().nextDouble() * 100 <= ConfigManagerMobgrowth.getCropsChance()) {
      event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(BlockGrowFeatureEvent event) {
    if (event.getLevel().getRandom().nextDouble() * 100 <= ConfigManagerMobgrowth.getSaplingChance()) {
      event.setCanceled(true);
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
            LetThemGrowMod.LOGGER.debug("Baby mob growth blocked from food");
            ParticleUtil.doSmoke(event.getLevel(), growing.position().x(),growing.position().y(),growing.position().z());
          }
        }
      }
    }
  }
}
