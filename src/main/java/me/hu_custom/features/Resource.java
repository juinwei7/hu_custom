package me.hu_custom.features;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class Resource implements Listener {

    @EventHandler
    public void Resourcecheck(PlayerResourcePackStatusEvent event){
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED){
            event.getPlayer().sendMessage("=============================");
            event.getPlayer().sendMessage(ChatColor.AQUA+ "您默認拒絕安裝資源包!!!");
            event.getPlayer().sendMessage(ChatColor.AQUA+ "請在進入前將資源包設定為開啟狀態");
            event.getPlayer().sendMessage(ChatColor.AQUA+ "若不會設定請聯絡官方人員協助。");
            event.getPlayer().sendMessage("=============================");
            return;
        }
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD){
            event.getPlayer().sendMessage(ChatColor.RED+ "資源包更新失敗，請聯絡幻嵐管理員");
            return;
        }
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED){
            event.getPlayer().sendMessage("資源包下載並更新完成");
            return;
        }
    }
}