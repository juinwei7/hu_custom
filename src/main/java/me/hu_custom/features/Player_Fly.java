package me.hu_custom.features;


import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Player_Fly implements Listener {

    String Permission_all = "canfaly.all";
    String Permission_vip6 = "canfaly.vip6";
    String Permission_player = "canfaly.player";

    List<String> world_list = new ArrayList<>(Arrays.asList(
            "stst",
            "stst02",
            "stst03"
    ));
    private boolean notfly_world(String worldname){
        for (String wd_line:world_list){
            if (wd_line.equalsIgnoreCase(worldname)){
                return false;
            }
        }
        return true;
    }


    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String worldname = event.getPlayer().getWorld().getName();
        if (!notfly_world(worldname)){return;}

        if (player.hasPermission(Permission_all)){
            player.setAllowFlight(true);
            seedMessage(player,"§a已開啟",false,"許可飛行時間 無限制");
            return;
        }
        if (isweekday() && player.hasPermission(Permission_player)){  //假日
            player.setAllowFlight(true);
            seedMessage(player,"§a已開啟",true,"許可飛行時間 12:00 - 04:00");
            return;
        }
        if (player.hasPermission(Permission_vip6) && isFlightAllowedNow(14,23)){
            player.setAllowFlight(true);
            seedMessage(player,"§a已開啟",false,"許可飛行時間 14:00 - 00:00");
            return;
        }
        if (player.hasPermission(Permission_player) && isFlightAllowedNow(18,21)){
            player.setAllowFlight(true);
            seedMessage(player,"§a已開啟",false,"許可飛行時間 18:00 - 22:00");
            return;
        }
        if (player.hasPermission(Permission_vip6)){
            seedMessage(player,"§c已關閉",false,"許可飛行時間 14:00 - 00:00");
            return;
        }
        if (player.hasPermission(Permission_player)){
            seedMessage(player,"§c已關閉",false,"許可飛行時間 18:00 - 22:00");
        }
    }


    @EventHandler
    public void ResidenceChanged(PlayerJumpEvent event) {
        Player player = event.getPlayer();

            String worldname = event.getPlayer().getWorld().getName();
            if (!notfly_world(worldname)){return;}

            if (player.hasPermission(Permission_all)) {
                player.setAllowFlight(true);
                return;
            }
            if (isweekday() && player.hasPermission(Permission_player)) {
                player.setAllowFlight(true);
                return;
            }
            if (player.hasPermission(Permission_vip6) && isFlightAllowedNow(14, 23)) {
                player.setAllowFlight(true);
                return;
            }
            if (player.hasPermission(Permission_player) && isFlightAllowedNow(18, 21)) {
                player.setAllowFlight(true);
                return;
            }
        }
    private boolean isweekday() {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY && isFlightAllowedNow(12,23) || isFlightAllowedNow(0,3)){
            return true;
        }
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY && isFlightAllowedNow(12,23) || isFlightAllowedNow(0,3)){
            return true;
        }
        return currentDate.getDayOfWeek() == DayOfWeek.SUNDAY && isFlightAllowedNow(12, 23);
    }


    private boolean isFlightAllowedNow(int START,int END) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        return currentHour >= START && currentHour <= END;
    }

    void seedMessage(Player player, String message,Boolean weekday,String lore) {
        if (weekday) {
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――――――");
            player.sendMessage("   " + message + " §f飛行模式 §7(假日福利)");
            player.sendMessage("§7 ");
            player.sendMessage("    §7" + lore);
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――――――");
        }else {
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――――――");
            player.sendMessage("   " + message + " §f飛行模式");
            player.sendMessage("§7 ");
            player.sendMessage("    §7" + lore);
            player.sendMessage(ChatColor.GRAY + "―――――――――――――――――――――――――");
        }
    }

}
