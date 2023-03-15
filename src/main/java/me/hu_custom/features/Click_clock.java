package me.hu_custom.features;


import me.hu_custom.Main;
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
        if (!Main.clock_enabled) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item != null && item.getType() == Material.CLOCK) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.isSneaking()) {
                    if (!Main.clock_shiftRight.equals("xxx")) {
                        player.chat(Main.clock_shiftRight);
                    }
                } else if (!Main.clock_right.equals("xxx")) {
                    player.chat(Main.clock_right);
                }
            } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (player.isSneaking()) {
                    if (!Main.clock_shiftLeft.equals("xxx")) {
                        player.chat(Main.clock_shiftLeft);
                    }
                } else if (!Main.clock_left.equals("xxx")) {
                    player.chat(Main.clock_left);
                }
            }
        }
    }

}
