package me.hu_custom.features;

import me.hu_custom.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.util.BoundingBox;
import sun.jvm.hotspot.ui.ObjectHistogramPanel;

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


        if (Main.eggspawner_on) {
            if (!entityTypes.contains(e.getEntityType())) {
                e.setCancelled(true);
                e.getSpawner().getLocation().getBlock().setType(Material.AIR);

                if (p.size() != 0) {
                    for (Entity player : p) {
                        player.sendMessage(ChatColor.RED + "已自動移除您附近非原版生怪磚");
                        Bukkit.getLogger().info("[生怪磚]移除"+player_5+" 類型:"+e.getEntityType());

                    }
                    return;

                }
            }
        }
    }
}
