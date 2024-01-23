package me.hu_custom.command;

import me.hu_custom.DataBase.DataBase;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import me.hu_custom.util.log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.UUID;
import java.util.function.Supplier;

public class TaxCommand implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;
        String uuid = String.valueOf(player.getUniqueId());
        String TABLE = "taxplayer";
        String money = DataBase.taxloadData(TABLE,"taxmoney",uuid);
        Timestamp time = Timestamp.valueOf(DataBase.taxloadData(TABLE,"expiretime",uuid));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time_dc = dateFormat.format(time);

        if (lable.equalsIgnoreCase("taxcheck")) {
            if (DataBase.taxBoolean(TABLE,uuid)){  //獲取是否有該玩家在在列表
                player.sendMessage("§7§m                                      ");
                player.sendMessage("§a " + player.getName() + " ➜");
                player.sendMessage("§7");
                player.sendMessage("§f  上次收稅時間 " + time_dc);
                player.sendMessage("§f  上次收稅金額 " + money);
                player.sendMessage("§7");
                player.sendMessage("§7§m                                      ");
            }
        }
        return false;
    }

    @EventHandler
    void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = event.getPlayer().getWorld().getName();
        String uuid = String.valueOf(event.getPlayer().getUniqueId());
        String name = event.getPlayer().getName();
        String TABLE = "taxplayer";
        // 获取当前时间
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        if (Config.getConfig().getString(Config.TAX_enabled_world).contains(world)){
            if (DataBase.taxBoolean(TABLE,uuid)){ //獲取是否有該玩家在在列表
                String time = DataBase.taxloadData(TABLE,"expiretime",uuid);

                Timestamp playertax = Timestamp.valueOf(time);
                // 獲取毫秒數表示的時間戳
                long currentTimeMillis = currentTimestamp.getTime();
                long playertaxMillis = playertax.getTime();
                // 計算兩個時間戳之間的毫秒差
                long differenceMillis = currentTimeMillis - playertaxMillis;
                // 轉換為天數
                long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);

                if (differenceDays > 15){ //檢查是否大於15天
                    applyTax(uuid,player, name, currentTimestamp);
                }
            }else {
                DataBase.taxsaveData(TABLE,uuid,name,"0",currentTimestamp);
            }
        }
    }


    public void applyTax(String uuid,Player player,String name,Timestamp currentTimestamp) {

        // 使用 Bukkit 调度器延迟执行 applyTax 方法
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            double vat = Main.econ.getBalance(player);
            int vatint = (int) vat;

                    for (String entry : Config.getTAX_moneylist()) {
                        String[] parts = entry.split(",");
                        if (parts.length == 2) {
                            int amount = Integer.parseInt(parts[0]);
                            int taxRate = Integer.parseInt(parts[1]);
                            if (vatint >= amount) {

                                // 在这里处理税金逻辑，例如扣除税金
                                double taxAmount = (vatint * taxRate / 100.0);
                                double newBalance = vatint - taxAmount;

                                // 你可以根据你的需求执行其他操作，比如更新玩家余额
                                Main.econ.withdrawPlayer(player, vatint * (taxRate * 0.01));
                                player.sendMessage("§f============ 執行扣稅 ============");
                                player.sendMessage("§e納稅玩家: " + player.getName());
                                player.sendMessage(" ");
                                player.sendMessage("§e稅前: $" + vatint);
                                player.sendMessage("§e稅後: $" + (int) newBalance);
                                player.sendMessage(" ");
                                player.sendMessage("§e執行稅率: " + taxRate + "%");
                                player.sendMessage("§f================================");
                                log.log("扣稅玩家: " + player.getName() + "稅前 " + vatint + "稅率 " + taxRate + "%" ,"TaxPlayer");
                                String TABLE = "taxplayer";
                                DataBase.taxsaveData(TABLE,uuid,name, String.valueOf(taxAmount),currentTimestamp);
                                break;
                            }
                        }
                    }
        }, 60L); // 60 ticks 等于 3 秒（20 ticks/秒）
    }
}
