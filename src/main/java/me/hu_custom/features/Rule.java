package me.hu_custom.features;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Rule implements Listener {

    private final Map<UUID, Long> Rule_cooldowns = new HashMap<>();
    private final Map<UUID, Long> PING_cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME_SECONDS = 600;

    @EventHandler
    public void InteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Location xyz = event.getPlayer().getLocation();
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(xyz);

        if (check_cooldown(player,1,1)) {

            if (block != null && player != null && res == null) {
                if (block.getType().equals(Material.CHEST)) {
                    Rule_cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                    player.sendMessage("§f〔提醒〕§c抄家是不被允許的，若您正在抄家請立即停止!");

                }
            }

        }
    }

    @EventHandler
    public void Rule_EDBE(PlayerJoinEvent event) {

            Player player = event.getPlayer();

            if (player != null) {
                int ping = player.getPing();

                if (player.hasPermission("bossshop.current.vip")) {
                    if (check_cooldown(player, 2, 3)) {
                        if (ping > 160) {
                            PING_cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                            player.sendMessage("§7§m                                           §r");
                            player.sendMessage("");
                            player.sendMessage("§f〔提醒〕§e您的PING值高於正常水平，建議您開客服單領取新的進入(IP)節點!");
                            player.sendMessage("§f〔提醒〕§eDISCORD ➡ https://discord.huanlan.org/");
                            player.sendMessage("");
                            player.sendMessage("§7§m                                           §r");
                        }
                    }
            }
        }
    }

    private boolean check_cooldown(Player player,int type,double multiple){

        if (type==1){
            if (Rule_cooldowns.containsKey(player.getUniqueId())) {
                double cooldownExpiration = (double)(Rule_cooldowns.get(player.getUniqueId()) + (COOLDOWN_TIME_SECONDS * 1000 * multiple));
                double currentTime = System.currentTimeMillis();
                return currentTime > cooldownExpiration;
            }else {
                return true;
            }
        }
        if (type==2){
            if (PING_cooldowns.containsKey(player.getUniqueId())) {
                double cooldownExpiration = (double)(Rule_cooldowns.get(player.getUniqueId()) + (COOLDOWN_TIME_SECONDS * 1000 * multiple));
                double currentTime = System.currentTimeMillis();
                return currentTime > cooldownExpiration;
            }else {
                return true;
            }
        }
        return false;
    }
}
