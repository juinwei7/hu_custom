package me.hu_custom.listeners;

import me.hu_custom.command.marry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Supplier;

import static me.hu_custom.Main.instance;

public class playerchat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (marry.waitingForInput && event.getPlayer()==marry.e) {
            event.setCancelled(true);
            //String player1 = marry.e.getName();
            String player2 = event.getMessage();
            String marrycommand = "marry marry " + player2;

            Bukkit.getScheduler().runTask(instance, () -> marry.e.performCommand(marrycommand));

            marry.waitingForInput = false;
        }
    }
}
