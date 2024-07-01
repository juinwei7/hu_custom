package me.hu_custom.command;

import me.hu_custom.util.Luckperms_hook;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NO_Drop implements CommandExecutor, Listener {

    // 这个字段没有被使用，可能可以删除
    private Map<UUID, Boolean> dropCooldowns = new HashMap<>();
    private JavaPlugin plugin;
    public NO_Drop(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dropCooldowns = new HashMap<>();
    }
    String Permission = "nodrop.use";



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // 如果命令发送者不是玩家，可以返回 false 或发送一条消息
            return false;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (label.equalsIgnoreCase("dr")) {
            if (player.hasPermission(Permission)) {
                Luckperms_hook.removeperr(player,Permission);
                seedMessage(player, "§c已關閉");
                return true; // 如果命令执行成功，返回 true
            } else {
                Luckperms_hook.addperr(player,Permission);
                seedMessage(player, "§a已開啟");
            }
        }

        return false;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!player.hasPermission(Permission)){
            return;
        }

            if (hasDropCooldown(playerUUID)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,  TextComponent.fromLegacyText(ChatColor.RESET + "已成功丟棄"));
            } else {
                addDropCooldown(playerUUID);
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,  TextComponent.fromLegacyText(ChatColor.RESET + "已取消丟棄物品，若要丟棄請連按兩下丟棄鍵(或輸入 /dr 關閉該功能)"));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        removeDropCooldown(playerUUID);
                    }
                }.runTaskLater(plugin, 20L); // 2秒冷却，40 ticks = 2秒
            }
    }

    private boolean hasDropCooldown(UUID playerUUID) {
        return dropCooldowns.containsKey(playerUUID);
    }

    private void addDropCooldown(UUID playerUUID) {
        dropCooldowns.put(playerUUID, true);
    }

    private void removeDropCooldown(UUID playerUUID) {
        dropCooldowns.remove(playerUUID);
    }


    private void seedMessage(Player player, String message) {
        player.sendMessage(ChatColor.GRAY + "――――――――――――――――");
        player.sendMessage("   " + message + " §f防丟棄");
        player.sendMessage(ChatColor.GRAY + "――――――――――――――――");
        // 或者使用 player.sendMessage(ChatColor.RED + "已開啟防丟棄");
    }
}
