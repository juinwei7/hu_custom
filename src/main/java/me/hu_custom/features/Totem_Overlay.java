package me.hu_custom.features;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static me.hu_custom.util.Color_St.Color_List;
import static me.hu_custom.util.Color_St.Color_st;

public class Totem_Overlay implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack holding = event.getCursor();
        ItemStack clicked = event.getCurrentItem();


        if (holding == null || holding.getType() != Material.TOTEM_OF_UNDYING) return;

        if (event.getClick() == ClickType.DOUBLE_CLICK || event.getClick() == ClickType.LEFT) {
            if (clicked == null || clicked.getType() == Material.AIR) {
                event.setCancelled(true);

                event.setCurrentItem(holding.clone());
                holding.setAmount(0);

            } else if (clicked.getType() == holding.getType()) {
                event.setCancelled(true);


                if (clicked.getAmount() + holding.getAmount() > 64) {
                    clicked.setAmount(clicked.getAmount() + holding.getAmount() - 64);
                    holding.setAmount(64);
                } else {
                    clicked.setAmount(clicked.getAmount() + holding.getAmount());
                    holding.setAmount(0);
                }
            }
        }

    }


    public static class TotemTask extends BukkitRunnable {
        @Override
        public void run() {

            for (Player player : Main.instance.getServer().getOnlinePlayers()) {
                if (player.getInventory().contains(Material.TOTEM_OF_UNDYING)) {

                    if (player.getInventory().contains(Material.TOTEM_OF_UNDYING, 2)) {
                        //has more than 2
                        int totemAmount = 0;
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item == null) continue;
                            if (item.getType() == Material.TOTEM_OF_UNDYING) {
                                totemAmount += item.getAmount();
                                item.setAmount(0);
                            }
                        }
                        ItemStack newTotem = new ItemStack(Material.PAPER);
                        NBTItem nbti = new NBTItem(newTotem);
                        String stackedTotemNBT = Config.getConfig().getString(Config.Totem_Overlay_nbt);
                        String[] split = stackedTotemNBT.split(",");
                        nbti.setString(split[0], split[1]);
                        int toto_model = Config.getConfig().getInt(Config.Totem_Overlay_model);
                        nbti.setInteger("CustomModelData", toto_model);
                        newTotem = nbti.getItem();

                        ItemMeta itemMeta = newTotem.getItemMeta();
                        String stackedItemName = Config.getConfig().getString(Config.Totem_Overlay_name);
                        itemMeta.setDisplayName(Color_st(stackedItemName));
                        List<String> toto_lore = Config.getTotem_Overlay_lore_list();
                        itemMeta.setLore(Color_List(toto_lore));
                        if (true) { // 附魔
                            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        }


                        newTotem.setItemMeta(itemMeta);

                        newTotem.setAmount(totemAmount - 1);

                        player.getInventory().addItem(newTotem, new ItemStack(Material.TOTEM_OF_UNDYING));
                    }
                } else if (player.getInventory().firstEmpty() != -1) {
                    //dont have any totem

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item == null) continue;
                        String stackedTotemNBT = Config.getConfig().getString(Config.Totem_Overlay_nbt);
                        String[] split = stackedTotemNBT.split(",");
                        NBTItem nbti = new NBTItem(item);
                        if (nbti.getString(split[0]).equals(split[1])) {
                            item.setAmount(item.getAmount() - 1);
                            player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                            break;
                        }
                    }

                }
                    /*
                    for (ItemStack item : player.getInventory().getContents()) {

                    }*/
            }

        }
    }
}