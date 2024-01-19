package me.hu_custom.features;

import me.hu_custom.Main;
import me.hu_custom.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EXP_LIMIT implements Listener {
    @EventHandler
    public void JoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // 创建一个异步任务，延迟3秒执行
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            // 获取玩家当前经验等级
            double playerExpLevel = player.getLevel();

            // 检查玩家游戏模式是否为生存模式
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                if (playerExpLevel > 9999) {
                    // 设置玩家的等级为9999
                    player.setLevel(9999);
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + "§c伺服器最大等級限制為: Lv.9999");
                }
            }
        }, 40L); // 60 ticks = 3秒
    }
}