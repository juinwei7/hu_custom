package me.hu_custom.features;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.util.Config;
import me.hu_custom.util.WarpItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class warpitem implements Listener {

    private Map<String, Long> cooldownMap = new HashMap<>(); // 记录物品的冷却结束时间

    private final Material[] materials_list = {
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.GOLDEN_AXE
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        ItemStack item = player.getInventory().getItemInMainHand();

        List<String> warp_namelist = WarpItem.getWarp_namelist();
        List<String> Item_namelist = WarpItem.getItem_namelist();

        if(offHandItem.getType() != Material.AIR) {
            if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) { //只偵測主手
                return;
            }
        }

        if (item == null || item.getType().isAir()){
            return;
        }
        Material material = item.getType();
        for (Material m:materials_list){
            if (m.equals(material)){
                return;
            }
        }

        if (event.getAction().name().contains("RIGHT") && player.isSneaking()) { // 只在右键点击时触发 && 蹲下

            String playername = player.getName();
            if (isOnCooldown(playername)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,  TextComponent.fromLegacyText(ChatColor.RED + "傳送正在冷卻中，請稍後在嘗試"));
                return;
            }

            for (String nbtkey01 : warp_namelist) {
                String[] parts = nbtkey01.split(",");
                String nbtkey = parts[0];  // 'nbtkey'
                String nbtvalue = parts[1];  // nbtvatvalue
                String command = parts[2];  // 执行的指令command

                NBTItem nbtItem = new NBTItem(item);
                if (nbtItem.hasTag(nbtkey)) { //確認有一樣key
                    String itemnbtkey = nbtItem.getString(nbtkey);
                    if (itemnbtkey.contains(nbtvalue)) {
                        String Commandwarp = command.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Commandwarp);
                        setCooldown(playername);
                        return;
                    }
                }
            }
            for (String nbtkey02 : Item_namelist) {
                String[] parts = nbtkey02.split(",");
                String name = parts[0];  // 'nbtkey'
                String command = parts[1];  // 执行的指令command

                String itemname = item.getItemMeta().getDisplayName();
                    if (itemname.contains(name)) {
                        String Commandwarp = command.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Commandwarp);
                        setCooldown(playername);
                        return;
                    }
                }
            }
        }


    private boolean isOnCooldown(String player) {
        if (cooldownMap.containsKey(player)) {
            long cooldownEndTime = cooldownMap.get(player);
            long currentTime = System.currentTimeMillis();
            return currentTime < cooldownEndTime; // 当前时间小于冷却结束时间，表示还在冷却中
        }
        return false; // 没有记录，不在冷却中
    }

    private void setCooldown(String itemName) {
        long cooldownEndTime = System.currentTimeMillis() + (5 * 1000);
        cooldownMap.put(itemName, cooldownEndTime);
    }



}
