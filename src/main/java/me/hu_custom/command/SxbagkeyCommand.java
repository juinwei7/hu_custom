package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Duration;

public class SxbagkeyCommand implements CommandExecutor {

    String perr = "sx.can_click_gui17";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
            Player player = (Player) sender;
            if (player.hasPermission(perr)){
                removeperr(player,perr);
                seedMessage(player,"§a已開啟",true);



            }else {
                addperr(player,perr);
                seedMessage(player,"§c已關閉",false);
                PlayerInventory inv = player.getInventory();
                ItemStack item17 = inv.getItem(17);
                if (item17 != null && !item17.getType().isAir()) {
                    NBTItem nbtItem = new NBTItem(item17);
                    String nbtst = nbtItem.getString("SX-Attribute-Name");
                    if (nbtst.contains("sx_player_gui17")){
                        inv.setItem(17,null);
                    }
                }
            }

        return false;
    }

    void seedMessage(Player player, String message,Boolean chk) {
        if (chk) {
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――");
            player.sendMessage("   " + message + " §f飾品背包快捷鍵");
            player.sendMessage("§7 ");
            player.sendMessage("    §7切換分流新增快捷鍵");
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――");
        }else {
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――");
            player.sendMessage("   " + message + " §f飾品背包快捷鍵");
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――");
        }
    }

    private void addperr(Player player, String permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        // 获取用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // 添加权限节点
        user.data().add(Node.builder(permission).expiry(Duration.ofDays(30)).build());

        // 保存用户数据
        luckPerms.getUserManager().saveUser(user);
    }

    private void removeperr(Player player, String permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        // 获取用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // 添加权限节点
        //user.data().remove(Node.builder(permission).build());
        user.data().add(Node.builder(permission).value(false).expiry(Duration.ofHours(1)).build());

        // 保存用户数据
        luckPerms.getUserManager().saveUser(user);
    }
}
