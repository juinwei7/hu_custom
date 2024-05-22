package me.hu_custom.util.item;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class custom_item {

    public ItemStack im_mu(){

        ItemStack item = new ItemStack(Material.CLOCK);

        ItemMeta im_mu = item.getItemMeta();
        im_mu.setDisplayName("§f✧ §e選單 §f✧");
        im_mu.setLore(Arrays.asList("§f右鍵 ➝ 開啟幻嵐選單","§f左鍵 ➝ 開啟快捷選單","","§c❖ §fshift+f 開啟幻嵐選單 §c❖"));
        item.setItemMeta(im_mu);

        return item;
    }

}
