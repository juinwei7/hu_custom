package me.hu_custom.command;

import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Resource implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            String url = Config.getConfig().getString(Config.Resource_urlt);
            Player e = (Player) sender;
            String prefix = Config.getConfig().getString(Config.prefix);


            if (Config.isResourceenabled()) {
                if (e.hasPermission(Config.getConfig().getString(Config.Lobby_perr))) {
                    e.getPlayer().sendMessage(prefix + "§d開始更新...");
                    if (url == null) {
                        e.getPlayer().sendMessage(prefix + "§c未找到材質包，請通知管理員。");
                    }

                    e.getPlayer().sendMessage(prefix + "§6正在更新材質包，請稍後...");
                    e.getPlayer().setResourcePack(url);
                } else {
                    e.getPlayer().sendMessage(prefix + "§c該世界不提供更新資源包");
                }
            } else {
                e.getPlayer().sendMessage(prefix + "§c該世界不提供更新資源包");

            }

        }
        return false;
    }
}