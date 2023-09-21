package thirtyvirus.perkagapples.events;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import thirtyvirus.perkagapples.PerkAGapples;
import thirtyvirus.perkagapples.items.vulture_aid;
import thirtyvirus.perkagapples.items.web_grenade;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

import java.util.Random;

public class UberEvent implements Listener {

    Random rand = new Random();

    // handle consuming custom gapples
    @EventHandler
    private void onGappleEat(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() != Material.ENCHANTED_GOLDEN_APPLE || !Utilities.isUber(item)) return;
        UberItem uber = Utilities.getUber(item); Player player = event.getPlayer();

        // call repeating function for new active effects
        switch (uber.getClass().getSimpleName()) {
            case "double_tap":
            case "juggernog":
            case "phd_flopper":
            case "quick_revive":
            case "speed_cola":
            case "stamin_up":
            case "vulture_aid":
            case "widows_wine":
                PerkAGapples.perk_a_player(player, uber.getClass().getSimpleName());
                break;
            default:
                return;
        }

        if (uber.getClass().getSimpleName().equals("speed_cola")) {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
           double baseValue = attribute.getBaseValue();

            if (baseValue != 16) {
                attribute.setBaseValue(16); player.saveData();
            }
        }

        Bukkit.getLogger().info("" + PerkAGapples.what_perks_player(player).toString());

        // finalize gapple consumption, disable vanilla effects
        event.setCancelled(true);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
        event.getPlayer().getInventory().remove(item);
        Utilities.scheduleTask(()-> event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.POTION_BREAK, 1), 20);
    }

    @EventHandler
    private void onPlayerKillEntity(EntityDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) return;
        if (event.getEntity().getKiller() == null) return;

        // give player random extra goodies when killing mobs if using vulture aid
        Player player = (Player)event.getEntity().getKiller();
        if (PerkAGapples.is_player_perked(player, "vulture_aid")) {
            event.getDrops().addAll(vulture_aid.dropRandomGoodies());
        }

    }

    @EventHandler
    private void onPlayerDamaged(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();

        // half all incoming damage if using Juggernog
        if (PerkAGapples.is_player_perked(player, "juggernog")) {
            event.setDamage(event.getDamage() / 2);
        }

        // remove explosion damage if using PhD Flopper
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            if (PerkAGapples.is_player_perked(player, "phd_flopper")) {
                event.setCancelled(true);
            }
        }

        // remove fall damage and cause mini explosion if using PhD Flopper
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (PerkAGapples.is_player_perked(player, "phd_flopper")) {
                spawnInstantTNT(player.getLocation(), calcFallExplosion(player), 1);
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    private void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player)event.getDamager();
        LivingEntity target = (LivingEntity)event.getEntity();

        // damage all enemies a second time if using double tap
        if (PerkAGapples.is_player_perked(player, "double_tap")) {
            Utilities.scheduleTask(()-> {
                target.damage(event.getFinalDamage());
            }, 20);
        }

        // small chance to activate web grenade ability if using widow's wine
        if (PerkAGapples.is_player_perked(player, "widows_wine")) {
            if (rand.nextInt(10) > 8) web_grenade.webExplosion(target.getLocation());
        }

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        double baseValue = attribute.getBaseValue();

        // disable hit cooldown if using speed cola, re-enable if not
        if (PerkAGapples.is_player_perked(player, "speed_cola")) {
            if (baseValue != 16) { attribute.setBaseValue(16); player.saveData(); }
        } else { if (baseValue != 4) { attribute.setBaseValue(4); player.saveData(); } }
    }

    @EventHandler
    private void onPlayerGetAttacked(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof LivingEntity)) return;
        Player player = (Player)event.getEntity();
        LivingEntity attacker = (LivingEntity)event.getDamager();

        // give slowness and hurt enemies who hit you if using widow's wine
        if (PerkAGapples.is_player_perked(player, "widows_wine")) {

            ItemStack webGrenade = UberItems.getItem("web_grenade").makeItem(1);
            if (player.getInventory().containsAtLeast(webGrenade, 1)) {
                player.getInventory().remove(webGrenade);
                web_grenade.webExplosion(attacker.getLocation());
            }

        }

    }

    @EventHandler
    private void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();

        // shoot second arrow if using double tap
        if (PerkAGapples.is_player_perked(player, "double_tap")) {
            Utilities.scheduleTask(()-> {
            shootAdditionalArrow(event, player);
            }, 5);
        }
    }

    @EventHandler
    private void onHungerDeplete(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();

        // give player 70% chance not to lose hunger if using stamin-up
        if (PerkAGapples.is_player_perked(player, "stamin_up")) {
            if (rand.nextInt(10) > 3) { event.setCancelled(true); }
        }
    }

    @EventHandler
    private void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && !Utilities.getEntityTag(event.getEntity(), "delete").equals("")) {
            // delete any extra arrows that land
            if (event.getHitBlock() != null) event.getEntity().remove();
        }
    }

    @EventHandler
    private void onBlockLand(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) return;
        FallingBlock block = (FallingBlock)event.getEntity();

        // make sure that thrown web grenades don't get placed as blocks
        if (Utilities.getEntityTag(block, "webgrenade").equals("asd")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDie(PlayerDeathEvent event) {
        // remove perks when a player dies
        PerkAGapples.unperk_a_player(event.getEntity());

        // verify that the speedcola boost is gone
        AttributeInstance attribute = event.getEntity().getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        double baseValue = attribute.getBaseValue();
        if (baseValue != 4) { attribute.setBaseValue(4); event.getEntity().saveData(); }
    }

    private void shootAdditionalArrow(EntityShootBowEvent event, Player player) {
        Arrow centerArrow = (Arrow) event.getProjectile();

        Arrow newArrow = event.getEntity().getWorld().spawnArrow(event.getEntity().getLocation().clone().add(0,2,0), event.getProjectile().getVelocity(), event.getForce() * 2, 0f);
        newArrow.setVelocity(newArrow.getVelocity().multiply(1.5)); newArrow.setShooter(event.getEntity());
        if (centerArrow.getBasePotionData().getType() != PotionType.UNCRAFTABLE) newArrow.setBasePotionData(centerArrow.getBasePotionData());
        newArrow.setFireTicks(centerArrow.getFireTicks()); newArrow.setCritical(centerArrow.isCritical());
        newArrow.setKnockbackStrength(centerArrow.getKnockbackStrength()); newArrow.setGlowing(centerArrow.isGlowing());
        Utilities.tagEntity(newArrow, player.getName(), "delete");
    }

    private static int calcFallExplosion(Player player) {
        int height = (int)player.getFallDistance();

        height /= 3;
        if (height > 8) return 8;
        else return Math.max(height, 1);
    }

    private static void spawnInstantTNT(Location location, int power, int delay) {
        Utilities.scheduleTask(() -> {
            TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class);
            tnt.setYield(power); tnt.setFuseTicks(1);
        }, delay);

    }

}
