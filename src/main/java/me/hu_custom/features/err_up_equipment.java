package me.hu_custom.features;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;


public class err_up_equipment implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        //防止玩家只用鐵砧更動物品
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.SMITHING || clickedInventory != null && clickedInventory.getType() == InventoryType.ANVIL) {
            ItemStack resultItem = clickedInventory.getItem(2);
            ItemStack resultItem0 = clickedInventory.getItem(0);

            if (resultItem != null) {
                if (resultItem0 != null) {
                    ItemMeta item = resultItem.getItemMeta();
                    String message = Config.getConfig().getString(Config.prefix + Config.smithing_table_message);
                    if (item.hasLore()) {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(message);
                        //Bukkit.getLogger().info(resultItem.getItemMeta().getDisplayName());
                    }
                }
            }
        }
        //防止玩家複製地圖畫 書本
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.CARTOGRAPHY || clickedInventory != null && clickedInventory.getType() == InventoryType.WORKBENCH) {
            Player player = (Player) event.getWhoClicked();
            String playerUUID = player.getUniqueId().toString();
            ItemStack resultItem = clickedInventory.getItem(2);
            ItemStack resultItem0 = clickedInventory.getItem(0);


            for (int i = 0; i < 9; i++) {
                ItemStack item = clickedInventory.getItem(i);
                if (item != null && !item.getType().equals(Material.AIR)) {
                    NBTItem nbt = new NBTItem(item);
                    if (nbt.hasKey("BindStatus")) {
                        String nbtst = nbt.getString("BindStatus");
                        String message = Config.getConfig().getString(Config.prefix + Config.Book_BindStatus);
                        if (!nbtst.equals(playerUUID)) {
                            event.setCancelled(true);
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().sendMessage(message);
                            return;
                        }
                    }
                }
            }

            if (resultItem != null) {
                if (resultItem0 != null) {
                    NBTItem nbti = new NBTItem(resultItem0);
                    if(nbti.hasKey("BindStatus")) {
                        String nbtst = nbti.getString("BindStatus");
                        String message = Config.getConfig().getString(Config.prefix + Config.Map_BindStatus);
                        if (!nbtst.equals(playerUUID)) {
                            event.setCancelled(true);
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().sendMessage(message);
                            //Bukkit.getLogger().info(resultItem.getItemMeta().getDisplayName());
                        }
                    }
                }
            }
        }
    }
}