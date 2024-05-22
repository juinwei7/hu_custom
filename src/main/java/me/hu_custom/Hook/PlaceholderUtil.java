package me.hu_custom.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hu_custom.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceholderUtil extends PlaceholderExpansion {

    public static Map<String, String> dataMap = new HashMap<>();

    @Override
    public @NotNull String getIdentifier() {
        return "huc";
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        // 检查占位符
        if (identifier.equals("boss_01")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_01");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }
        if (identifier.equals("boss_02")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_02");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }
        if (identifier.equals("boss_03")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_03");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }
        if (identifier.equals("boss_04")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_04");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }
        if (identifier.equals("boss_05")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_05");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }
        if (identifier.equals("boss_06")) {
            // 调用您的方法获取MythicMob ID
            String mythicMobId = dataMap.get("boss_06");
            return mythicMobId != null ? mythicMobId : "Unknown";
        }

        return null;
    }

}
