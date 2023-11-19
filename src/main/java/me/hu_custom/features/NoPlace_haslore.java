package me.hu_custom.features;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoPlace_haslore implements Listener {

/*
    private Map<UUID, Boolean> PlaceCooldowns;
    private JavaPlugin plugin;

    public NoPlace_haslore(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        PlaceCooldowns = new HashMap<>();
    }

 */

    ItemStack[] item = {
            new ItemStack(Material.PAPER)
    };

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();
        String itemid = String.valueOf(item.getType());

        NBTItem nbti = new NBTItem(item);

        if(nbti.hasKey("PublicBukkitValues")){ //防止神社件至
            return;
        }
/*
        if (hasPlaceCooldown(playerUUID)) {
            //event.setCancelled(false);
            //player.sendMessage(ChatColor.RED + "已成功放置");

        } else

 */
            if (itemMeta.hasLore()) {
                if (!itemid.contains("SHULKER_BOX")) {
                    //addPlaceCooldown(playerUUID);
                    event.setCancelled(true);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "該物品似乎是RPG物品，已取消放置"));
/*
            new BukkitRunnable() {
                @Override
                public void run() {
                    //removePlaceCooldown(playerUUID);
                }
            }.runTaskLater(plugin, 20L); // 2秒冷却，40 ticks = 2秒

 */

                }
        }
    }

/*
    private boolean hasPlaceCooldown(UUID playerUUID) {
        return PlaceCooldowns.containsKey(playerUUID);
    }

    private void addPlaceCooldown(UUID playerUUID) {
        PlaceCooldowns.put(playerUUID, true);
    }

    private void removePlaceCooldown(UUID playerUUID) {
        PlaceCooldowns.remove(playerUUID);
    }

 */
}