package me.hu_custom.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.hu_custom.Main.instance;


public class marry implements CommandExecutor {
    public static boolean waitingForInput = false;
    public static Player e = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
                e = (Player) sender;

                e.getPlayer().sendMessage("§b請在10秒內輸入你想告白的對象");
                waitingForInput =true;

            Bukkit.getScheduler().runTask(instance, () -> {
                Bukkit.getScheduler().runTaskLater(instance, () -> {
                    if (waitingForInput) {
                        waitingForInput = false;
                        e.getPlayer().sendMessage("§c已取消輸入");
                    }
                }, 200L);
            });
            }

        return false;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (marry.waitingForInput && event.getPlayer()==marry.e) {
            event.setCancelled(true);
            String player2 = event.getMessage();
            String marrycommand = "marry marry " + player2;

            Bukkit.getScheduler().runTask(instance, () -> marry.e.performCommand(marrycommand));

            marry.waitingForInput = false;
        }
    }
}