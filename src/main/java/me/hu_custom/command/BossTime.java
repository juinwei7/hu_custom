package me.hu_custom.command;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import me.hu_custom.DataBase.DataBase;
import me.hu_custom.Hook.PlaceholderUtil;
import me.hu_custom.util.Config;
import me.hu_custom.util.cooldown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.hu_custom.util.cooldown.setCooldown;


public class BossTime implements CommandExecutor, Listener {

    public static Map<String, Integer> boss_cooldown = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        // 获取当前时间

        if (lable.equalsIgnoreCase("bosstime")) {
            if (args.length >= 2) {
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
                    if (!hasbossname) {
                        return false;
                    } //處理不在名單上的bossname

                    Map timebossValue = DataBase.loadData(args[1]);
                    Timestamp expiretime = (Timestamp) timebossValue.get("expiretime");

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH 時 mm 分 ss 秒");
                        String formattedTime = dateFormat.format(expiretime);

                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            player.sendMessage("§7=========================");
                            player.sendMessage("§7");
                            player.sendMessage("§e " + bossname_st + " ➜");
                            player.sendMessage("§7");
                            player.sendMessage("§f   ⏲下次出生時間: " + formattedTime);
                            player.sendMessage("§7");
                            player.sendMessage("§7=========================");
                        }

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
        return false;
    }



    @EventHandler
    public void DeathEvent(EntityDeathEvent event) {

        String worldname = event.getEntity().getWorld().getName();

        if (worldname.contains("stst") || worldname.contains("stst02") || worldname.contains("stst03")) {

            Entity bukkitEntity = event.getEntity();
            if (!MythicBukkit.inst().getMobManager().isMythicMob(bukkitEntity)) {
                return;
            } //不是myth生物直接返回
            if (cooldown.isOnCooldown("boss_cooldown")) {
                return;
            } //冷卻中 直接返回

            List<String> spawner_list = Config.getBossTimeing_SpawnerName();
            for (String spawner : spawner_list) {
                String[] parts = spawner.split(",");
                String spawnerName = parts[0];
                String cooldType = parts[1];
                String sql_bossname = parts[2];
                MythicSpawner mobsspawner = MythicBukkit.inst().getSpawnerManager().getSpawnerByName(spawnerName);
                if (mobsspawner != null) {
                    if (cooldType.equals("Cooldown")) {
                        int mobtime = mobsspawner.getRemainingCooldownSeconds(); //獲取冷卻時間
                        boss_cooldown.put(sql_bossname, mobtime);
                    }
                    if (cooldType.equals("Warmup")) {
                        int mobtime = mobsspawner.getRemainingWarmupSeconds(); //獲取冷卻時間
                        boss_cooldown.put(sql_bossname, mobtime);
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : boss_cooldown.entrySet()) {
                String sql_bossname = entry.getKey();
                Integer cooldown = entry.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, cooldown);
                Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

                DataBase.saveData(sql_bossname, timestamp);
            }
            setCooldown("boss_cooldown", 120);
            Bukkit.getLogger().info("已執行---boss_cooldown");
        }

    }
    @EventHandler
    public void join(PlayerJoinEvent event) {
        List<String> spawner_list = Config.getBossTimeing_SpawnerName();
        if (!cooldown.isOnCooldown("boss_cooldown")) {
            for (String spawner : spawner_list) {
                String[] parts = spawner.split(",");
                String sql_bossname = parts[2];
                Map timebossValue = DataBase.loadData(sql_bossname);
                if (timebossValue != null) {
                    Timestamp expiretime = (Timestamp) timebossValue.get("expiretime");
                    if (expiretime != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = dateFormat.format(expiretime);
                        PlaceholderUtil.dataMap.put(sql_bossname, formattedTime);
                    }
                }
            }
        }
    }
}