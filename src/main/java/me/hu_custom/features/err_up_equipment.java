package me.hu_custom.features;

import me.hu_custom.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class err_up_equipment implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();


        if (clickedInventory != null && clickedInventory.getType() == InventoryType.SMITHING || clickedInventory != null && clickedInventory.getType() == InventoryType.ANVIL) {
            ItemStack resultItem = clickedInventory.getItem(2);
            ItemStack resultItem0 = clickedInventory.getItem(0);

            if (resultItem != null) {
                if (resultItem0 != null) {
                    ItemMeta item = resultItem.getItemMeta();
                    if (item.hasLore()) {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(Main.prefix + Main.smithing_table_message);
                        //Bukkit.getLogger().info(resultItem.getItemMeta().getDisplayName());
                    }
                }
            }
        }
    }
}