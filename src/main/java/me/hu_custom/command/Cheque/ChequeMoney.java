package me.hu_custom.command.Cheque;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import me.hu_custom.util.cooldown;
import me.hu_custom.util.log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;


public class ChequeMoney implements CommandExecutor , Listener {
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;
        String noperrPermission = Config.getConfig().getString(Config.ChequeMoney_perr);

        if (lable.equalsIgnoreCase("chequemoney")) {
            if(!Config.isChequeMoneyenabled()){
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_noperr));
                return false;
            }
            if (noperrPermission != null && player.hasPermission(noperrPermission)) {
                int sendmoney = 0;
                double vat = Main.econ.getBalance(player);
                int vatint = (int) vat;

                if (args.length == 0) {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_tointerror));
                    return true;
                }
                if (args.length == 1) {
                    try {
                        sendmoney = Integer.parseInt(args[0]);
                        // 現在你可以使用整數變數 secondArg 來處理第二個參數
                    } catch (NumberFormatException e) {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_tointerror));
                        return false;
                    }
                    if(sendmoney < vatint && sendmoney > 1000 && sendmoney < 10000000){

                        Main.econ.withdrawPlayer(player, sendmoney);
                        ItemStack itemStack = new ItemStack(hub_item_paper(player,sendmoney));
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§a成功創建支票，金額 $" + sendmoney);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeMoney_discordnb) + " :blue_square: 創建遊戲幣支票 " + player.getName() + ", " + sendmoney; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                    }else {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_nomoney));
                    }

                }else {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_tointerror));
                }

            }else {
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_noperr));
            }
        }

        return false;
    }

    public ItemStack hub_item_paper(Player player,int value) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:00");
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, 40); //時間延遲
        Date expirationTime = calendar.getTime();


        List<String> itemlore = Config.getChequeMoneyitemList();

        String currentTimeStr = sdf.format(currentTime);
        String expirationTimeStr = sdf.format(expirationTime);
        String value_String = String.valueOf(value);

        List<String> updatedLore = new ArrayList<>();
        for (String loreLine : itemlore) {
            loreLine = loreLine.replace("%player%", player.getName());
            loreLine = loreLine.replace("%time%", currentTimeStr);
            loreLine = loreLine.replace("%over_time%", expirationTimeStr);
            loreLine = loreLine.replace("%Money_value%",value_String);
            updatedLore.add(loreLine);
        }

        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Config.getConfig().getString(Config.ChequeMoney_chequeitem_name).replace("%Money_value%",value_String));
        itemMeta.setLore(updatedLore);
        itemStack.setItemMeta(itemMeta);


        //////////////// NBT /////////////////////
        String player_id = player.getName();
        String uuid = String.valueOf(player.getUniqueId());

        NBTItem nbt1 = new NBTItem(itemStack);
        nbt1.setString("hu_ItemName","ChequeMoney");
        nbt1.setInteger("CustomModelData", 2001);
        nbt1.setInteger("money_value", value);
        nbt1.setString("hub_player", player_id);
        nbt1.setString("hub_uuid", uuid);

        log.log("創建金額 " + value_String + ", " + player.getName() ,"ChequeMoney");

        return nbt1.getItem(); // 返回带有更新描述的物品堆栈

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ////////// 防止副手 ///////////
        ItemStack offHandItem = event.getPlayer().getInventory().getItemInOffHand();
        if (offHandItem.getType() != Material.AIR) {
            if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) { //只偵測主手
                return;
            }
        }
        ////////// 開始判定 ///////////
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction().name().contains("RIGHT")) { // 只在右键点击时触发
            if (!item.getType().isAir()) {
                NBTItem nbti = new NBTItem(item);
                if (nbti.hasKey("hu_ItemName") && nbti.getString("hu_ItemName").equals("ChequeMoney")) {
                    if (!cooldown.isOnCooldown("ChequeMoney")) {
                        item.setAmount(item.getAmount() - 1);

                        int value = nbti.getInteger("money_value");
                        String hub_player = nbti.getString("hub_player");

                        double points_D = Main.econ.getBalance(player);
                        ;
                        Main.econ.depositPlayer(player, value);
                        double points01 = Main.econ.getBalance(player);
                        ;
                        log.log("核銷金額 " + value + ", " + player.getName() + " ， 生成者: " + hub_player, "ChequeMoney");

                        player.sendMessage("§7============================");
                        player.sendMessage(Config.getConfig().getString(Config.ChequeMoney_message_success));
                        player.sendMessage("提取前擁有: $" + points_D);
                        player.sendMessage("提取後擁有: $" + points01);
                        player.sendMessage("");
                        player.sendMessage("§c若領取失敗，請開客服單並提供截圖。");
                        player.sendMessage("§7=============================");
                        cooldown.setCooldown("ChequeMoney", 2);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeMoney_discordnb) + " :red_square: 使用遊戲幣支票 " + player.getName() + ", " + value; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                    }else {
                        cooldown.sendActionBar(player,Config.getConfig().getString(Config.ChequeExp_message_cooldown));
                    }
                }
            }
        }
    }
}
