package thirtyvirus.perkagapples.events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.perkagapples.PerkAGapples;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.Utilities;

public class UberEvent implements Listener {

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

        Bukkit.getLogger().info("" + PerkAGapples.what_perks_player(player).toString());

        // finalize gapple consumption, disable vanilla effects
        event.setCancelled(true);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
        event.getPlayer().getInventory().remove(item);
        Utilities.scheduleTask(()-> event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.POTION_BREAK, 1), 20);
    }

}
