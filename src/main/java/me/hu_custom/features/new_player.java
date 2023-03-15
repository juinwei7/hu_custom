package me.hu_custom.features;


import me.hu_custom.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;


public class new_player implements Listener {


    @EventHandler
    public void sendplayer(PlayerCommandPreprocessEvent event) {
        String permis = Main.new_player_perr_st;




        if (Main.new_player_perr_on) {
            Player e = event.getPlayer();
            if (!event.getMessage().contains("huanlancheck") && !event.getMessage().contains("warp hu_tch") && !event.getMessage().contains("s")) {
                if (!e.hasPermission(permis)) {
                    e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + Main.new_player_perr);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void join_new(PlayerJoinEvent event) {
        Player play = event.getPlayer();
        World world = event.getPlayer().getWorld();

        if (Main.new_player_perr_on) {
            if (!play.hasPermission(Main.new_player_perr_st)) {
                if (world.getName().equals("Lobby")) {
                    Bukkit.getLogger().info("新玩家");
                    event.getPlayer().teleport(new Location(world, 167.2, 81, 41.5, -178.5f, -9.6f));
                }
            }
        }
    }
}
