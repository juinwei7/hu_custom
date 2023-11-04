package me.hu_custom.remove;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RemoveEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        // 检查是否是玩家点击背包
        if (clickedInventory == null || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack heldItem = player.getInventory().getItemInMainHand();


        if (heldItem != null && heldItem.hasItemMeta()) {
            NBTItem nbti1 = new NBTItem(heldItem);
            if (nbti1.hasKey("MYTHIC_TYPE") && nbti1.getString("MYTHIC_TYPE").equals("仙域之劍max")) {
                ItemMeta itemMeta = heldItem.getItemMeta();
                if (itemMeta.hasLore()) {
                    for (String lore : itemMeta.getLore()) {
                        // 检查 Lore 中是否包含特定内容
                        if (lore.contains("Ben2015Ben") || lore.contains("Albert0510")) {
                            // 执行你想要的操作，例如发送消息或者执行命令
                            player.getInventory().setItemInMainHand(null);
                            player.sendMessage("§c被認定為違禁品，若有錯誤請截圖開客服單，若無截圖不予處理！");
                            break; // 如果你只想执行一次操作
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClick(PlayerInteractEvent event) {
        ItemStack heldItem = event.getPlayer().getItemInHand();

        if (heldItem != null && heldItem.hasItemMeta()) {
            ItemMeta itemMeta = heldItem.getItemMeta();
            NBTItem nbti1 = new NBTItem(heldItem);
            if (nbti1.hasKey("MYTHIC_TYPE") && nbti1.getString("MYTHIC_TYPE").equals("仙域之劍max")) {
                if (itemMeta.hasLore()) {
                    for (String lore : itemMeta.getLore()) {
                        // 检查 Lore 中是否包含特定内容
                        if (lore.contains("Player94147190") || lore.contains("Fantong_OwO") || lore.contains("jarry1234567") || lore.contains("Ben2015Ben") || lore.contains("Albert0510")) {
                            // 执行你想要的操作，例如发送消息或者执行命令
                            event.getPlayer().sendMessage("§c被認定為違禁品，若有錯誤請截圖開客服單，若無截圖不予處理！");

                            // 从玩家的手中移除物品
                            event.getPlayer().getInventory().setItemInMainHand(null);

                            break; // 如果你只想执行一次操作
                        }
                    }
                }
            }
        }
    }
}