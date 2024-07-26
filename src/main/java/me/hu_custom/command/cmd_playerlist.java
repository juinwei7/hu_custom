package me.hu_custom.command;

import me.hu_custom.util.Cmd_PlayeyList;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class cmd_playerlist implements CommandExecutor, Listener {


    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;

        if (lable.equalsIgnoreCase("cmd_playerlist")) {
            if(player.isOp()){

                List<String> playerlist = Cmd_PlayeyList.getPlayer_list_ls();

                String command = Cmd_PlayeyList.getCmd_playeyList().getString(Cmd_PlayeyList.command);

                for (String name:playerlist){
                    assert command != null;
                    command = Cmd_PlayeyList.getCmd_playeyList().getString(Cmd_PlayeyList.command);
                    command = command.replaceAll("%player%",name);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);


                    try {
                        Thread.sleep(10); // 1000毫秒 = 1秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }

            }
        }
        return false;
    }
}
