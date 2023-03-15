package me.hu_custom.command;

import me.hu_custom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Resource implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0){
            String url= Main.Resource_urlt;
            Player e = (Player) sender;

            if(Main.Resource_on) {
                e.getPlayer().sendMessage("開始更新");
                if (url == null) {
                    e.getPlayer().sendMessage("未找到材質包，請通知管理員。");
                }

                e.getPlayer().sendMessage("正在更新材質包，請稍後...");
                e.getPlayer().setResourcePack(url);
            }else {
                e.getPlayer().sendMessage("該世界不提供更新資源包");
            }

        }

        return false;
    }
}