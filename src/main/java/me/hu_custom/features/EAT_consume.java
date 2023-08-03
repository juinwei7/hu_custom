package me.hu_custom.features;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EAT_consume implements Listener {
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        String item_nmae = item.getItemMeta().getDisplayName();

        if (item != null && item.getType() == Material.BREAD && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();

            int foodLevel = 0;
            int healthLevel = 0;

            for (String line : lore) {
                if (line.contains("恢復飽食度")) {
                    Pattern pattern = Pattern.compile("(?<=恢復飽食度).*?([0-9]+)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        foodLevel = Integer.parseInt(matcher.group(1));
                    }
                } else if (line.contains("恢復生命值")) {
                    Pattern pattern = Pattern.compile("(?<=恢復生命值).*?([0-9]+)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        healthLevel = Integer.parseInt(matcher.group(1));
                    }
                }
            }

            if (foodLevel > 0) {
                player.setFoodLevel(player.getFoodLevel() - 5 + foodLevel); //扣掉麵包原本恢復再加上自訂義
            }

            if (healthLevel > 0) {
                player.setHealth(Math.min(player.getHealth() + healthLevel, player.getMaxHealth()));
            }


            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "你消耗了 " + item_nmae + ChatColor.GREEN + " 恢復成功 "));
        }
    }
}
