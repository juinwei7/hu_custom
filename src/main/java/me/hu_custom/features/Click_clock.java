package me.hu_custom.features;

import me.hu_custom.util.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Click_clock implements Listener {

    @EventHandler
    public void onInventoryClick(PlayerInteractEvent event) {
        if (!Config.isClock()) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.getType() == Material.CLOCK) {
            String messageKey = null;
            Action action = event.getAction();

            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                messageKey = player.isSneaking() ? Config.clock_shift_right : Config.clock_right;
            } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                messageKey = player.isSneaking() ? Config.clock_shift_left : Config.clock_left;
            }

            if (messageKey != null) {
                String message = Config.getConfig().getString(messageKey);
                if (message != null && !message.equals("xxx")) {
                    player.chat(message);
                }
            }
        }
    }
}
