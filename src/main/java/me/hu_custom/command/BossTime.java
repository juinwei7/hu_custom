package me.hu_custom.command;

import me.hu_custom.DataBase.DataBase;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BossTime implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        if (lable.equalsIgnoreCase("bosstime")) {
            if (args.length >= 2) {
                if (args[0].equals("set") && args.length == 3) {

                    if (sender instanceof Player) { //防止非管理人員輸入
                        Player player = (Player) sender;
                        if (!player.isOp()) {
                            return false;
                        }

                    }
                    String bossname = args[1];
                    List<String> Bosstimes_list = Config.getBossTimeing_TimeCooldown();
                    for (String Bosstimes : Bosstimes_list){
                        String[] parts = Bosstimes.split(",");
                        String boss_name = parts[0];
                        String killname = args[2];
                        int bosscooldown = Integer.parseInt(parts[1]);  // 物品数量

                        if (bossname.equals(boss_name)){
                            calendar.add(Calendar.MINUTE, bosscooldown/60);
                            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                            DataBase.saveData(bossname, timestamp, killname);
                            if (sender instanceof Player){
                                Player player = (Player) sender;
                                player.sendMessage(bossname + " 已更新時間");
                            }
                            Bukkit.getLogger().info(bossname + " 已更新時間");
                        }

                    }
                }
                if (args[0].equals("check")) {

                    List<String> Bosstimes_list = Config.getBossTimeing_TimeCooldown();
                    String bossname_st = null;
                    Boolean hasbossname = false;

                    for (String Bosstimes_line : Bosstimes_list) { //更換bossname名稱
                        String[] parts = Bosstimes_line.split(",");
                        String boss_name = parts[0];
                        String killname = parts[2];

                        if (args[1].equals(boss_name)) {
                            bossname_st = killname;
                            hasbossname = true;
                            break;
                        }
                    }
                    if (!hasbossname){return false;} //處理不在名單上的bossname

                    Map timebossValue = DataBase.loadData(args[1]);
                    Timestamp expiretime = (Timestamp) timebossValue.get("expiretime");
                    String killname = (String) timebossValue.get("killname");

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH 時 mm 分 ss 秒");
                        String formattedTime = dateFormat.format(expiretime);

                        if (sender instanceof Player){
                            Player player = (Player) sender;
                            player.sendMessage("§7=========================");
                            player.sendMessage("§7");
                            player.sendMessage("§e " + bossname_st + " ➜");
                            player.sendMessage("§7");
                            player.sendMessage("§f   ⏲下次出生時間: " + formattedTime);
                            player.sendMessage("§f   \uD83C\uDFAE上次擊殺玩家: " + killname);
                            player.sendMessage("§7");
                            player.sendMessage("§7=========================");
                        }

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }


            }else {
                Bukkit.getLogger().info("指令錯誤");
            }

        }
        return false;
    }
}
