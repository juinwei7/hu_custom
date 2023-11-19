package me.hu_custom.command;

import me.hu_custom.util.item.custom_item;
import me.hu_custom.util.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Item_get implements CommandExecutor {

    ItemStack im_mu = new custom_item().im_mu();
    String no_permissions = ChatColor.RED + "您沒有權限";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player p = (Player) sender;
        if (lable.equalsIgnoreCase("huget")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "請重新輸入");
                return true;
            }
            String a = args[0];
            switch (a) {
                case "clock":
                    if(Config.isClock()){
                    p.getInventory().addItem(im_mu);
                    p.updateInventory();
                    break;}
                    else {
                        p.sendMessage(no_permissions);
                        break;
                    }

                default:
                    p.sendMessage(ChatColor.RED + "未找到物品!");
            }
            return true;
        }
        return false;
    }
}