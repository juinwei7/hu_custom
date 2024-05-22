package me.hu_custom.features.Safari;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import de.Linus122.SafariNet.API.Status;
import de.Linus122.SafariNet.API.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class SafariNetListener implements Listener {


    public void playerCatchEntity(Player player, Entity entity, Status status) {
        Location loc = player.getLocation();
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(loc);
        Bukkit.getLogger().info(player.getName());
        if (res != null) {
            String resname = res.getOwner();
            Bukkit.getLogger().info(resname);
            if (resname == player.getName()) {
                status.setCancelled(true);
            }
        }
    }

    public void playerReleaseEntity(Player player, Entity entity, Status status) {

    }


}
