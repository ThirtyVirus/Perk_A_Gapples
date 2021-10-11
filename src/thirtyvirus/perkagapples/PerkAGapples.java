package thirtyvirus.perkagapples;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thirtyvirus.perkagapples.events.UberEvent;
import thirtyvirus.perkagapples.items.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.AbilityType;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;

import java.util.*;

public class PerkAGapples extends JavaPlugin {

    private static Map<Player, ArrayList<String>> perkedPlayers = new HashMap<>();
    private static int activePerkaColas = 0;

    public void onEnable() {

        // enforce UberItems dependancy
        if (Bukkit.getPluginManager().getPlugin("UberItems") == null) {
            this.getLogger().severe("Perk-A-Gapples requires UberItems! disabled because UberItems dependency not found");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // schedule repeating task for processing Perk-A-Gapples active effects
        // in ticks
        int activePerkaColasDelay = 5;
        activePerkaColas = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, PerkAGapples::activeEffects, activePerkaColasDelay, activePerkaColasDelay);

        // register thirtyvirus.perkagapples.events and UberItems
        registerEvents();
        registerUberMaterials();
        registerUberItems();

        // post confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");
    }
    public void onDisable() {
        // cancel scheduled tasks
        Bukkit.getScheduler().cancelTask(activePerkaColas);

        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new UberEvent(), this);
    }

    private void registerUberItems() {

        UberItems.putItem("juggernog", new juggernog(Material.ENCHANTED_GOLDEN_APPLE, "Juggernog", UberRarity.VERY_SPECIAL,
                false, true, false,
                Collections.singletonList(new UberAbility("Juggernog", AbilityType.NONE, "♫ When you need some help to get by, something to make you feel strong. Reach for Juggernog tonight, sugar seduction delight! When you need to feel big and strong, reach for Juggernog tonight! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.GLISTERING_MELON_SLICE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GHAST_TEAR),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GHAST_TEAR),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GLISTERING_MELON_SLICE)), false, 1 )));

        UberItems.putItem("quick_revive", new quick_revive(Material.ENCHANTED_GOLDEN_APPLE, "Quick Revive", UberRarity.RARE,
                false, true, false,
                Collections.singletonList(new UberAbility("Quick Revive", AbilityType.NONE, "♫ When everything's, been draggin' you down, grabbed you by the hair and pulled you to the ground, if you wanna get up, you need a little revive. If you wanna get up... YOU NEED A LITTLE REVIVE! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.COD),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.CARROT),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.KELP),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.COD)), false, 1 )));

        UberItems.putItem("speed_cola", new speed_cola(Material.ENCHANTED_GOLDEN_APPLE, "Speed Cola", UberRarity.UNCOMMON,
                false, true, false,
                Collections.singletonList(new UberAbility("Speed Cola", AbilityType.NONE, "♫ Your hands are slow, your movement's sluggish, your lack of speed, just brings you anguish. Just take a sip, you will move faster. Just try it now! And speed is mastered! Press those lips against the only one that really moves you. Speed Cola speeds up your life! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.SUGAR),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SWEET_BERRIES),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SWEET_BERRIES),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SUGAR)), false, 1 )));

        UberItems.putItem("double_tap", new double_tap(Material.ENCHANTED_GOLDEN_APPLE, "Double Tap", UberRarity.MYTHIC,
                false, true, false,
                Collections.singletonList(new UberAbility("Double Tap", AbilityType.NONE, "♫ Cowboys can't shoot slow or they'll end up below. When they need some help, they reach for the Root beer shelf (Ye-haa) Cowboys can't shoot slow, or they'll end up below, when they need some help, they reach for the Root beer shelf. YA THIRSTY PARTNER!? ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.JUNGLE_SAPLING),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SUGAR),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SUGAR),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.JUNGLE_SAPLING)), false, 1 )));

        UberItems.putItem("phd_flopper", new phd_flopper(Material.ENCHANTED_GOLDEN_APPLE, "PhD Flopper", UberRarity.MYTHIC,
                false, true, false,
                Collections.singletonList(new UberAbility("PhD Flopper", AbilityType.NONE, "♫ PhD, night-time scene. PhD, the streets are mean. PhD, the things I've seen, the good (PhD), the bad, and the in-between (Flopper). When you dive to prone, you are surely gonna own, PhD (Flopper)! When you dive to prone, it's gonna shake ya to the bone, all the zombies gonna groan, 'cause of PhD! PhD, the feelin's growing strong. PhD, so right that it feels wrong. PhD, like the chorus of a song (Flopper). PhD, not short but not too long. Slap your body to the floor, everybody needs some more, of your lovin', your explosive lovin'. When you dive to prone, it's gonna shake ya to the bone, all the zombies gonna groan, cause of PhD! (Flopper)! Damn straight! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.GUNPOWDER),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.BEETROOT),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.BEETROOT),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GUNPOWDER)), false, 1 )));

        UberItems.putItem("stamin_up", new stamin_up(Material.ENCHANTED_GOLDEN_APPLE, "Stamin-Up", UberRarity.MYTHIC,
                false, true, false,
                Collections.singletonList(new UberAbility("Stamin-Up", AbilityType.NONE, "♫ When you need some extra runnin', when you need some extra time, when you want to keep on gunnin', when you like a twist of lime. When you need to keep on moving, when you need a get-away, when you need to keep on groovin', when you need that vitamin K, babe, you know you want me! Let's run the extra mile! I'll open your eyes and I'll make you see! I'll make it worth your while! Stamin-up-Min-Up! Sounds like it's Staaaaamin-Up time! Oh yeah, drink it baby. Drink it! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.MUSHROOM_STEM),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.RABBIT_STEW),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.RABBIT_STEW),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.MUSHROOM_STEM)), false, 1 )));

        UberItems.putItem("vulture_aid", new vulture_aid(Material.ENCHANTED_GOLDEN_APPLE, "Vulture Aid", UberRarity.SPECIAL,
                false, true, false,
                Collections.singletonList(new UberAbility("Vulture Aid", AbilityType.NONE, "♫ I was looking for redemption, I was looking for a sign. I was searching for an answer, be it yours or be it mine. I was hoping for some insight, I was looking far too hard. I was searching for the wrong thing, now I don't know where to start! I spy with my little eye, something beginning with V! It's Vulture Aid, and it's not too late! Get Vulture Aid! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.FEATHER),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GLOWSTONE_DUST),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.GLOWSTONE_DUST),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.FEATHER)), false, 1 )));

        UberItems.putItem("widows_wine", new widows_wine(Material.ENCHANTED_GOLDEN_APPLE, "Widow's Wine", UberRarity.EPIC,
                false, true, false,
                Collections.singletonList(new UberAbility("Widow's Wine", AbilityType.NONE, "♫ When you're feeling kinda lonely, and your spouse is kinda dead, you feel like curling up with someone, in your empty little bed. There is danger around the corner, (CORNER!) And your love life's in decline, (DECLIIINNNEE!) You'll no longer be a martyr, with a little Widow's Wine (x3). With a little Widow's Wine you're mine... with Widow's Wine, you're mine! ♫")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.SPIDER_EYE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.FERMENTED_SPIDER_EYE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.APPLE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.FERMENTED_SPIDER_EYE),
                        new ItemStack(Material.GOLD_INGOT),
                        new ItemStack(Material.SPIDER_EYE)), false, 1 )));
    }
    private void registerUberMaterials() { }

    private static void activeEffects() {
        for (Player player : perkedPlayers.keySet()) {
            if (is_player_perked(player, "stamin_up")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
            }
        }
    }

    // add perk to a player
    public static void perk_a_player(Player player, String perk) {
        if (is_player_perked(player, perk)) return;

        if (perkedPlayers.containsKey(player))
            perkedPlayers.get(player).add(perk);
        else {
            ArrayList<String> list = new ArrayList<>();
            list.add(perk);
            perkedPlayers.put(player, list);
        }
    }

    // test if player has a perk
    public static boolean is_player_perked(Player player, String perk) {
        if (!perkedPlayers.containsKey(player)) return false;
        else return perkedPlayers.get(player).contains(perk);
    }

    // return the full list of perks the player has
    public static List<String> what_perks_player(Player player) {
        if (!perkedPlayers.containsKey(player)) return Collections.emptyList();
        else return perkedPlayers.get(player);
    }

    // remove all perks from a player
    public static void unperk_a_player(Player player) {
        perkedPlayers.remove(player);
    }
}