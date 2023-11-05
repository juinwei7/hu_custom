package me.hu_custom.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class cooldown {

    private static Map<String, Long> cooldownMap = new HashMap<>();

    public static boolean isOnCooldown(String itemName) {
        if (cooldownMap.containsKey(itemName)) {
            long cooldownEndTime = cooldownMap.get(itemName);
            long currentTime = System.currentTimeMillis();
            return currentTime < cooldownEndTime; // 当前时间小于冷却结束时间，表示还在冷却中
        }
        return false; // 没有记录，不在冷却中
    }

    public static void setCooldown(String itemName, int cooldownSeconds) {
        long cooldownEndTime = System.currentTimeMillis() + (cooldownSeconds * 1000);
        cooldownMap.put(itemName, cooldownEndTime);
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
