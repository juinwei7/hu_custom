package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
=========================
        裝備卷兌換
=========================
*/

public class Equipment_rollCommand implements CommandExecutor, Listener {

    static String GUINAME = Config.getConfig().getString(Config.Equipment_Gui_name); //獲取GUINAME

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;

        if (lable.equalsIgnoreCase("equipment_roll")) {
            if(Config.isEquipmentenabled()){  //確認config Equipment開啟
                Equipment_rollInventory((Player)sender);
            }


        }
        return false;
    }

    public static void Equipment_rollInventory(Player player) {

        Inventory inventory;
        inventory = Bukkit.createInventory(null, 27, GUINAME);
        inventory.setItem(26, createItem(Material.END_CRYSTAL, "開始兌換", 0));
        ItemStack def_glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", 300);
        for (int i = 0; i <= 26; i++) { // 将默认物品放入菜单的槽位
            if (i != 13 && i != 26) {
                inventory.setItem(i, def_glass);
            }
        }
        player.openInventory(inventory);
    }

    @EventHandler
    void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(GUINAME)) {
            int golt = event.getRawSlot();
            if (golt!=13 && golt<=26){ event.setCancelled(true); } //取消點擊

            if (golt == 26){
                ItemStack itemStack = event.getInventory().getItem(13);

                if (itemStack==null || itemStack.getType().equals(Material.AIR)){
                    player.sendMessage(Config.getConfig().getString(Config.Equipment_Messages_NOITEM));
                    return;
                }
                NBTItem nbtItem = new NBTItem(itemStack);
                List<String> EquipmentlIST = Config.getEquipment_list();
                for (String equi : EquipmentlIST) {
                    String[] parts = equi.split(",");
                    String nbtIdentifier = parts[0];  // 'stic01'
                    int needAmount = Integer.parseInt(parts[1]);  // 物品数量
                    String commandToExecute = parts[2];  // 执行的指令

                    if (nbtItem.hasTag("SX-Attribute-Name")) {
                        if (nbtItem.getString("SX-Attribute-Name").equals(nbtIdentifier)){
                            int Amount = itemStack.getAmount();
                            if (Amount>=needAmount){
                                itemStack.setAmount(Amount-needAmount);
                                String finalCommand = commandToExecute.replace("%player%", player.getName());
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
                                break;
                            }else {
                                String cannotAmount = Config.getConfig().getString(Config.Equipment_Messages_NOAmount).replace("%amount%", String.valueOf(needAmount));
                                player.sendMessage(cannotAmount);
                            }
                        }

                    }else {
                        player.sendMessage(Config.getConfig().getString(Config.Equipment_Messages_NoNbtTag));
                    }
                }
                // 进行字符串分割

            }

        }
    }


    @EventHandler
    void InventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(GUINAME)) {
            Player player = (Player) event.getPlayer();
            for (int i = 0; i < 27; i++) {
                ItemStack item = event.getInventory().getContents()[i];
                if (item == null || item.getType().isAir()) continue;
                if (i == 13) {
                    player.getInventory().addItem(item);
                }
            }
        }
    }

    private static ItemStack createItem(Material material, String name, int setCustomModelData) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("SX-Attribute-Name", "sx_gui");

        itemMeta.setCustomModelData(setCustomModelData);
        item.setItemMeta(itemMeta);
        return item;
    }
}
