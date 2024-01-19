package me.hu_custom.command.Cheque;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import me.hu_custom.util.cooldown;
import me.hu_custom.util.log;
import org.bukkit.Bukkit;
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

public class ChequeExp implements CommandExecutor, Listener {
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;
        String noperrPermission = Config.getConfig().getString(Config.ChequeExp_perr);

        if (lable.equalsIgnoreCase("chequeexp")) {
            if(!Config.isChequeExpenabled()){
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_noperr));
                return false;
            }
            if (noperrPermission != null && player.hasPermission(noperrPermission)) {
                int sendmoney = 0;
                double vat = player.getTotalExperience(); //總經驗值
                int vatleve = player.getLevel(); //等級

                if (args.length == 0) {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeExp_message_tointerror));
                    return true;
                }
                if (args.length == 1) {
                    try {
                        sendmoney = Integer.parseInt(args[0]);
                        // 現在你可以使用整數變數 secondArg 來處理第二個參數
                    } catch (NumberFormatException e) {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeExp_message_tointerror));
                        return false;
                    }
                    double sendmoney_tax = sendmoney*1.05; //x稅率 5%
                    if(sendmoney_tax < vat && vatleve >= 100 && sendmoney > 0){
                        Bukkit.getLogger().info(String.valueOf(vatleve));

                        player.giveExp((int) -sendmoney_tax);
                        ItemStack itemStack = new ItemStack(hub_item_paper(player,sendmoney));
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§a成功創建支票，EXP值 " + sendmoney);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeExp_discordnb) + " :blue_square: 創建經驗支票 " + player.getName() + ", " + sendmoney; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);

                    }else {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeExp_message_nomoney));
                    }

                }else {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeExp_message_tointerror));
                }

            }else {
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeExp_message_noperr));
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


        List<String> itemlore = Config.getChequeExpitemList();

        String currentTimeStr = sdf.format(currentTime);
        String expirationTimeStr = sdf.format(expirationTime);
        String value_String = String.valueOf(value);

        List<String> updatedLore = new ArrayList<>();
        for (String loreLine : itemlore) {
            loreLine = loreLine.replace("%player%", player.getName());
            loreLine = loreLine.replace("%time%", currentTimeStr);
            loreLine = loreLine.replace("%over_time%", expirationTimeStr);
            loreLine = loreLine.replace("%exp_value%",value_String);
            updatedLore.add(loreLine);
        }

        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Config.getConfig().getString(Config.ChequeExp_chequeitem_name).replace("%exp_value%",value_String));
        itemMeta.setLore(updatedLore);
        itemStack.setItemMeta(itemMeta);


        //////////////// NBT /////////////////////
        String player_id = player.getName();
        String uuid = String.valueOf(player.getUniqueId());

        NBTItem nbt1 = new NBTItem(itemStack);
        nbt1.setString("hu_ItemName","ChequeExp");
        nbt1.setInteger("CustomModelData", 2002);
        nbt1.setInteger("exp_value", value);
        nbt1.setString("hub_player", player_id);
        nbt1.setString("hub_uuid", uuid);

        log.log("創建金額 " + value_String + ", " + player.getName() ,"ChequeExp");

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
                if (nbti.hasKey("hu_ItemName") && nbti.getString("hu_ItemName").equals("ChequeExp")) {
                    if (!cooldown.isOnCooldown("ChequeExp")) {
                        item.setAmount(item.getAmount() - 1);

                        int value = nbti.getInteger("exp_value");
                        String hub_player = nbti.getString("hub_player");

                        double points_D = player.getTotalExperience();
                        player.giveExp(value);
                        double points01 = player.getTotalExperience();
                        log.log("核銷金額 " + value + ", " + player.getName() + " ， 生成者: " + hub_player, "ChequeExp");

                        player.sendMessage("§7============================");
                        player.sendMessage(Config.getConfig().getString(Config.ChequeExp_message_success));
                        player.sendMessage("提取前擁有: " + points_D + "EXP");
                        player.sendMessage("提取後擁有: " + points01 + "EXP");
                        player.sendMessage("");
                        player.sendMessage("§c若領取失敗，請開客服單並提供截圖。");
                        player.sendMessage("§7=============================");
                        cooldown.setCooldown("ChequeExp", 2);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeExp_discordnb) + " :red_square: 使用經驗支票 " + player.getName() + ", " + value; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                    }else {
                        cooldown.sendActionBar(player,Config.getConfig().getString(Config.ChequeMoney_message_cooldown));
                    }
                }
            }
        }
    }
}
