package me.hu_custom.command;

import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlimeChunk implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            Player e = (Player) sender;
            boolean SlimeChunk = e.getLocation().getChunk().isSlimeChunk();

            if (e.hasPermission("bossshop.server.server26") || e.isOp()) {
                if (SlimeChunk) {
                    e.getPlayer().sendMessage("§f=========================");
                    e.getPlayer().sendMessage("§6是否為史萊姆生成區塊: §a是");
                    e.getPlayer().sendMessage("§f=========================");
                } else{
                    e.getPlayer().sendMessage("§f=========================");
                    e.getPlayer().sendMessage("§6是否為史萊姆生成區塊: §c否");
                    e.getPlayer().sendMessage("§f=========================");
                }
            }
        }

        return false;
    }
}