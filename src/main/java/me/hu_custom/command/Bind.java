package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Bind implements CommandExecutor {
    public static ItemMeta unbindedItem(NBTItem nbtItem) {
        nbtItem.removeKey("BindStatus");
        ItemMeta itemMeta = nbtItem.getItem().getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.remove(0);
        for (int i = 0;i<lore.size();i++){
            if (lore.get(i).contains("綁定留言:")){
                lore.remove(i);
            }
        }
        itemMeta.setLore(lore);
        return itemMeta;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();
            double playervalue = Main.econ.getBalance(player);
            int value = Config.getConfig().getInt(Config.BIND_Settings_value);

            if (item == null || item.getType().isAir()) {
                // 你手上未持有任何物品!
                player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_MainHandIsAir));
            } else {

                NBTItem nbtItem = new NBTItem(item);

                switch (command.getName().toLowerCase()) {
                    case "bind":
                        String message = null;
                        boolean CheckMessage = false;
                        if (args.length >= 1 && args[0] != null) {
                            message = args[0];
                            if (args.length >= 2 && args[1] != null) {
                                    message = args[0] + " " + args[1];
                            }
                            if (message.length() <= 15){
                                CheckMessage = true;
                            }else {
                                player.sendMessage("§f〔幻嵐助手〕§r§c綁定留言請勿超過15字(含空格)，已更改成原始綁定!");
                            }
                        }
                        if (!nbtItem.hasKey("BindStatus")) {
                            if (playervalue >= value) { //玩家遊戲必須大於條件
                                Main.econ.withdrawPlayer(player, value);
                                // 已成功將物品綁定為您的!
                                nbtItem.setString("BindStatus", player.getUniqueId().toString());
                                ItemMeta itemMeta = nbtItem.getItem().getItemMeta();
                                List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
                                String playerName = player.getName();
                                String bindLore = Config.getConfig().getString(Config.BIND_Settings_BindLore1).replace("%playername%", playerName);
                                // &b✔ &f%playername% &a已綁定
                                lore.add(0, bindLore);
                                if (CheckMessage){
                                    lore.add(1,"§b✔ §f綁定留言: §7" + message);
                                }
                                itemMeta.setLore(lore);
                                item.setItemMeta(itemMeta);
                                player.setItemInHand(item);
                                player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_BindSuccessfully));
                            }else {
                                player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_NoMoney) + value);
                            }
                        }else {
                            // 此物品已被綁定過
                            player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_AlreadyBinded));
                        }

                        break;
                    case "unbind":
                        if (!nbtItem.hasKey("BindStatus")) {
                            // 此物並未被綁定 你想解除什麼?
                            player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_NotBinded));
                        } else {
                            String uuidOfItem = nbtItem.getString("BindStatus");
                            if (uuidOfItem.equals(player.getUniqueId().toString()) || player.hasPermission("hubind.op")) {
                                // 已成功將物品解除綁定
                                item.setItemMeta(unbindedItem(nbtItem));
                                player.setItemInHand(item);
                                player.sendMessage(Config.getConfig().getString(Config.BIND_Messages_UnbindSuccessfully));
                            }
                        }
                        break;
                }
            }
        }
        return true;
    }

}
