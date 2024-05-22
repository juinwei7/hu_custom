package me.hu_custom.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class check_perr implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("group.vip") || player.hasPermission("group.vip2") ||
                player.hasPermission("group.vip+") || player.hasPermission("group.vip2+") ||
                player.hasPermission("group.vip3") || player.hasPermission("group.vip4") ||
                player.hasPermission("group.vip5") || player.hasPermission("group.vip6") ||
                player.hasPermission("group.vip7") || player.hasPermission("group.vip8")){


            if (player.hasPermission("group.default")){
                String commane = "lp user "+ player.getName() +" parent remove default";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commane);
            }

        }

    }


}
