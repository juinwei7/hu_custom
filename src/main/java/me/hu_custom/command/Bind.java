package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Bind implements CommandExecutor {
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

    public static ItemMeta unbindedItem(NBTItem nbtItem) {
        nbtItem.removeKey("BindStatus");
        ItemMeta itemMeta = nbtItem.getItem().getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.remove(0);
        itemMeta.setLore(lore);
        return itemMeta;
    }

}
