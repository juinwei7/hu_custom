package me.hu_custom.features;

import me.hu_custom.Main;
import me.hu_custom.util.Config;
import net.craftersland.data.bridge.api.events.SyncCompleteEvent;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.units.qual.N;

public class sx_bag_perr implements Listener {

    @EventHandler
    void SyncComplete(SyncCompleteEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();

        if (player.isOp()) {
            return;
        }

        if (world.equalsIgnoreCase("Lobby")) {

            if (player.hasPermission("group.default") && !player.hasPermission("sx-attribute.bag.use")) {
                String command = "lp user " + player.getName() + " permission settemp sx-attribute.bag.use true 30d"; // 替换为你想要执行的命令
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                return;
            }
            if (!player.hasPermission("sx-attribute.bag.use")) {
                double player_money = Main.econ.getBalance(player);
                int take_money = (int) (Math.sqrt(player_money) * 100);
                if (take_money > 800000) {
                    take_money = 800000;
                }
                if (player_money < 100000) {
                    take_money = (int) (player_money / 10);
                }

                if (take_money < 0) {
                    take_money = 0;
                }
                Main.econ.withdrawPlayer(player, take_money);
                String command = "lp user " + player.getName() + " permission settemp sx-attribute.bag.use true 12d"; // 替换为你想要执行的命令
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                player.sendMessage(Config.getConfig().getString(Config.prefix) + "§b飾品系統已自動開通，本次開通花費 " + take_money + " ， 12天");

            }
        }
    }
}
