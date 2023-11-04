package me.hu_custom.features;

import com.google.common.eventbus.DeadEvent;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Piglin_Dorp implements Listener {
    @EventHandler
    public void Piglin(EntityDeathEvent e) {
        if (Config.isLimitevent()) {
            if (Config.isPiglinDorp()) {
                if (e.getEntityType().equals(EntityType.PIGLIN) || e.getEntityType().equals(EntityType.ZOMBIFIED_PIGLIN)) {

                    List<ItemStack> list = e.getDrops();
                    for (ItemStack itemStack : list) {
                        if (itemStack.getType().equals(Material.GOLDEN_SWORD)) {
                            e.getDrops().remove(itemStack);
                            break;
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void snow_dor(EntityPortalEnterEvent e){


        if(e.getEntityType()==EntityType.SNOWBALL){
            e.getEntity().remove();
        }
    }
}
