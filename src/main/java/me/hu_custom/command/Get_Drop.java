package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import me.hu_custom.util.Luckperms_hook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.hu_custom.util.cooldown.isOnCooldown;
import static me.hu_custom.util.cooldown.setCooldown;

public class Get_Drop implements CommandExecutor,Listener {

    String CanUse_AutoSell = "sx-attribute.useautosell";

    String disable_autosell = "sx-attribute.disable_autosell";

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (lable.equalsIgnoreCase("autosell")) {
                if (player.hasPermission(CanUse_AutoSell)){
                    if (player.hasPermission(disable_autosell)){
                        Luckperms_hook.removeperr(player,disable_autosell);
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§c已關閉 §e經濟貨幣類型 拾取自動販賣");
                        return false;
                    }else {
                        Luckperms_hook.addperr(player,disable_autosell);
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§a已開啟 §e經濟貨幣類型 拾取自動販賣");
                        return false;
                    }
                }else {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + "§c您沒有權限 開啟 或 關閉 該功能");
                    return false;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void getdrop(EntityPickupItemEvent event){
        ItemStack drop_item = event.getItem().getItemStack();

        if (drop_item != null) {
            if (event.getEntity() instanceof Player) {
                Player player = ((Player) event.getEntity()).getPlayer();
                if (player == null) {
                    return;
                }
                if (!player.hasPermission(CanUse_AutoSell)) {
                    return;
                }
                if (!player.hasPermission(disable_autosell)||player.isOp()) {
                    NBTItem nbtItem = new NBTItem(drop_item);
                    if (nbtItem.hasTag("MYTHIC_TYPE")) {
                        String MYTH_money = nbtItem.getString("MYTHIC_TYPE");

                        if (MYTH_money.contains("sx靈異金星")) {
                            int item_money = getNumbers(MYTH_money);
                            if (item_money > 0) {
                                int drop_am = drop_item.getAmount();
                                double give_money = drop_am * item_money;
                                Main.econ.depositPlayer(player, give_money);

                                event.setCancelled(true);
                                event.getItem().remove();
                                player.sendMessage(Config.getConfig().getString(Config.prefix) + "§b拾取經濟貨幣，完成自動販賣 獲得: $" + give_money);

                                if (!isOnCooldown(player.getName()+"_autosell_a")) {
                                    player.sendMessage(Config.getConfig().getString(Config.prefix) + "§a已開啟 §e經濟貨幣拾取自動販賣，如關閉請輸入[/autosell]");
                                    setCooldown(player.getName()+"_autosell_a",1200);
                                }
                            }
                        }
                    }
                }else {
                    if (!isOnCooldown(player.getName()+"_autosell")) {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§c已禁用 §e經濟貨幣拾取自動販賣，如開啟請輸入[/autosell]");
                        setCooldown(player.getName()+"_autosell",1200);
                    }
                }
            }
        }
    }

    public int getNumbers(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        StringBuilder numbers = new StringBuilder();

        while (matcher.find()) {
            numbers.append(matcher.group());
        }

        try {
            int number = Integer.parseInt(numbers.toString());
            return number;
        } catch (NumberFormatException e) {
            System.out.println("Error: money get Error");
        }

        return -1;
    }
}
