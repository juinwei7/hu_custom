package me.hu_custom.features;

import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class egg_spawner implements Listener {

    private List<EntityType> entityTypes = new ArrayList<EntityType>();

    @EventHandler
    public void spawner(SpawnerSpawnEvent e) {
        entityTypes.add(EntityType.PIG);
        entityTypes.add(EntityType.SPIDER);
        entityTypes.add(EntityType.CAVE_SPIDER);
        entityTypes.add(EntityType.SILVERFISH);
        entityTypes.add(EntityType.ZOMBIE);
        entityTypes.add(EntityType.SKELETON);
        entityTypes.add(EntityType.MAGMA_CUBE);
        entityTypes.add(EntityType.BLAZE);

        Location player_5 = e.getSpawner().getLocation();
        Collection<Entity> p = player_5.getWorld().getNearbyEntities(player_5, 10, 10, 10, null);

        if (Config.isLimitevent()) {
            if (Config.isEggspawner()) {
                if (!entityTypes.contains(e.getEntityType())) {
                    e.setCancelled(true);
                    e.getSpawner().getLocation().getBlock().setType(Material.AIR);

                    if (p.size() != 0) {
                        for (Entity player : p) {
                            player.sendMessage(ChatColor.RED + "已自動移除您附近非原版生怪磚");
                            Bukkit.getLogger().info("[生怪磚]移除" + player_5 + " 類型:" + e.getEntityType());

                        }
                        return;

                    }
                }
            }
        }
    }
        @EventHandler
        public void IE(PlayerInteractEvent event) {
                ItemStack item = event.getItem();
                if (item!=null && item.getType().equals(Material.ENDER_DRAGON_SPAWN_EGG)) {
                    event.setCancelled(true);
                    item.setAmount(0);
                    event.getPlayer().sendMessage(Config.getConfig().getString(Config.prefix) + "§c終界龍是不允許被釋放的!");
        }
    }
    @EventHandler
    public void onPIH(PlayerItemHeldEvent event) {
        // 獲取玩家的新手持物品槽位
        int newSlot = event.getNewSlot();
        ItemStack newItem = event.getPlayer().getInventory().getItem(newSlot);

        // 檢查新手持物是否是終界龍蛋
        if (newItem != null && newItem.getType() == Material.ENDER_DRAGON_SPAWN_EGG) {
            newItem.setAmount(0);
            event.getPlayer().sendMessage(Config.getConfig().getString(Config.prefix) + "§c終界龍是不允許被持有的!");
        }
    }
}
