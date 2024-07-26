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



public class err_up_equipment implements Listener {
    /*
    防止玩家隨意更動物品
    鐵砧 等等
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        //防止玩家只用鐵砧更動物品
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.SMITHING || clickedInventory != null && clickedInventory.getType() == InventoryType.ANVIL || clickedInventory != null && clickedInventory.getType() == InventoryType.GRINDSTONE) {
            String message = Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.smithing_table_message);
            ItemStack clickitem = event.getCurrentItem();

            if (clickitem != null && clickedInventory.getType() != InventoryType.SMITHING){
                if (clickitem.hasItemMeta() && clickitem.getItemMeta().hasLore()){
                    event.getWhoClicked().sendMessage(message);
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }
            }
            if (clickitem != null && clickedInventory.getType() != InventoryType.SMITHING){
                if (event.isShiftClick()) {
                    if (clickitem.hasItemMeta() && clickitem.getItemMeta().hasLore()) {
                        event.getWhoClicked().sendMessage(message);
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }
                }
            }


            if (clickedInventory.getType() == InventoryType.SMITHING){
                int solt = event.getRawSlot();
                if (event.isShiftClick() && solt==3){
                    event.getWhoClicked().sendMessage(Config.getConfig().getString(Config.prefix) + "§c升級裝備不開放 Shift 點擊");
                    event.setCancelled(true);
                    return;
                }
                ItemStack resultItem_0 = clickedInventory.getItem(0);
                ItemStack resultItem_1 = clickedInventory.getItem(1);
                if (resultItem_0 != null && resultItem_0.getType().equals(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)){
                    if (resultItem_1 != null && resultItem_1.hasItemMeta() && resultItem_1.getItemMeta().hasLore()) {
                        event.getWhoClicked().sendMessage(message);
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                    }
                }
            }

            /*
            if (resultItem != null) {
                if (resultItem0 != null) {
                    ItemMeta item = resultItem.getItemMeta();
                    String message = Ceonfig.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.smithing_table_message);
                    if (item.hasLore()) {
                        event.setCancelled(true);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(message);
                        //Bukkit.getLogger().info(resultItem.getItemMeta().getDisplayName());
                    }
                }
            }

             */
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
                        String message = Config.getConfig().getString(Config.prefix)+ Config.getConfig().getString(Config.Book_BindStatus);
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
                        String message = Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.Map_BindStatus);
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