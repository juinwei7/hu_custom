package me.hu_custom.features;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Rule implements Listener {

    private final Map<UUID, Long> Rule_cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME_SECONDS = 600;

    @EventHandler
    public void InteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Location xyz = event.getPlayer().getLocation();
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(xyz);

        if (Rule_cooldowns.containsKey(player.getUniqueId())) {
            long cooldownExpiration = Rule_cooldowns.get(player.getUniqueId()) + (COOLDOWN_TIME_SECONDS * 1000);
            long currentTime = System.currentTimeMillis();

            if (currentTime < cooldownExpiration) {
                return;
            }
        }

        if (block!=null && player!=null && res == null){
            if (block.getType().equals(Material.CHEST)){
                Rule_cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                player.sendMessage("§f〔提醒〕§c抄家是不被允許的，若您正在抄家請立即停止!");

            }
        }

    }
}
