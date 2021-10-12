package thirtyvirus.perkagapples.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thirtyvirus.perkagapples.PerkAGapples;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.*;

public class web_grenade extends UberItem {

    public web_grenade(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean rightClickAirAction(Player player, ItemStack item) {

        if (PerkAGapples.is_player_perked(player, "widows_wine")) {
            FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Bukkit.createBlockData(Material.COBWEB));
            block.setDropItem(false);
            block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(1));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 1, 1);
            Utilities.tagEntity(block, "asd", "webgrenade");
            processFallingWeb(block);
            return true;
        }
        else return false;
    }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return rightClickAirAction(player, item); }

    public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
    public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }
    public boolean activeEffect(Player player, ItemStack item) { return false; }

    private void processFallingWeb(FallingBlock block) {
        Utilities.scheduleTask(()->{

            if (block.isDead()) {
                webExplosion(block.getLocation());
            }
            else processFallingWeb(block);
        }, 2);
    }

    public static void webExplosion(Location location) {
        if (location == null || location.getWorld() == null) return;

        location.getWorld().playSound(location, Sound.ENTITY_BAT_HURT, 2, 0.5f);
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2, 2f);
        for (Entity e : location.getWorld().getNearbyEntities(location, 4, 4, 4)) {
            if (e instanceof LivingEntity && !(e instanceof Player)) {
                location.getWorld().playSound(location, Sound.ENTITY_SPIDER_HURT, 2, 2);
                LivingEntity en = (LivingEntity) e;
                en.damage(1);
                en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
            }
        }
    }
}
