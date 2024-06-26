package me.hu_custom.command.Cheque;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.DataBase.DataBase;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.lumine.mythic.core.utils.RandomUtil.random;


public class ChequeMoney implements CommandExecutor , Listener {

    static String database_name = "chequemoney";

    public static String generateRandomString(int Number) {
        StringBuilder sb = new StringBuilder(Number);
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player player = (Player) sender;
        String noperrPermission = Config.getConfig().getString(Config.ChequeMoney_perr);

        if (lable.equalsIgnoreCase("chequemoney")) {
            if(!Config.isChequeMoneyenabled()){
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeMoney_message_noperr));
                return false;
            }
            if (noperrPermission != null && player.hasPermission(noperrPermission)) {
                int sendmoney = 0; //輸入金額
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
                    double sendmoney_tax = sendmoney*1.08; //x稅率 8%
                    if(sendmoney_tax < vatint && sendmoney >= 1000 && sendmoney < 10000000){ //檢查money是否足夠

                        String Identification = generateRandomString(10);
                        Calendar calendar = Calendar.getInstance();
                        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

                        Main.econ.withdrawPlayer(player, sendmoney_tax);
                        ItemStack itemStack = new ItemStack(hub_item_paper(player,sendmoney,Identification)); //製作支票(含辨識碼8位數)
                        player.getInventory().addItem(itemStack);
                        DataBase.chequesaveData(database_name,Identification,player.getName(), String.valueOf(sendmoney),timestamp);
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

    public ItemStack hub_item_paper(Player player,int value,String Identification) {

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

        if (value > 3000000){
            updatedLore.add(" ");
            updatedLore.add("§7大額支票辨識碼: " + Identification);
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

        if (value > 3000000) {nbt1.setString("hub_identification", Identification);}

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
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction().name().contains("RIGHT")) { // 只在右键点击时触发
            if (!item.getType().isAir()) {
                NBTItem nbti = new NBTItem(item);
                if (nbti.hasKey("hu_ItemName") && nbti.getString("hu_ItemName").equals("ChequeMoney")) {
                    if (!cooldown.isOnCooldown("ChequeMoney")) {

                        /*
                          處理大額支票 辨識碼
                         */
                        int value = nbti.getInteger("money_value");
                        String hub_player = nbti.getString("hub_player");
                        if (nbti.hasTag("hub_identification")){
                            String identification = nbti.getString("hub_identification");
                            if (DataBase.chequeBooldean(database_name,identification)){
                                String name_use = DataBase.chequeloadData(database_name,"name_use",identification);
                                if (name_use!=null){
                                    player.sendMessage("§f該支票已被使用，使用玩家 " + name_use);
                                    String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeMoney_discordnb) + " :rotating_light: 正在嘗試使用用過的支票 " + player.getName() + ", 辨識碼" + identification; // 替换为你想要执行的命令
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                                    cooldown.setCooldown("ChequeMoney", 4);
                                    return;
                                }else {
                                    Calendar calendar = Calendar.getInstance();
                                    Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                                    DataBase.chequesaveData(database_name,identification,player.getName(), player.getName(),timestamp);
                                }
                            }else {
                                player.sendMessage("§c該支票無創建紀錄，請截圖並開客服單詢問");
                                String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeMoney_discordnb) + " :rotating_light: 正在嘗試使用未知的支票 " + player.getName() + ", 辨識碼" + identification; // 替换为你想要执行的命令
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                                cooldown.setCooldown("ChequeMoney", 4);
                                return;
                            }
                        }

                        //開始正常執行

                        item.setAmount(item.getAmount() - 1);

                        double points_D = Main.econ.getBalance(player);

                        Main.econ.depositPlayer(player, value);
                        double points01 = Main.econ.getBalance(player);

                        log.log("核銷金額 " + value + ", " + player.getName() + " ， 生成者: " + hub_player, "ChequeMoney");

                        player.sendMessage("§7============================");
                        player.sendMessage(Config.getConfig().getString(Config.ChequeMoney_message_success));
                        player.sendMessage("提取前擁有: $" + points_D);
                        player.sendMessage("提取後擁有: $" + points01);
                        player.sendMessage("");
                        player.sendMessage("§c若領取失敗，請開客服單並提供截圖。");
                        player.sendMessage("§7=============================");
                        cooldown.setCooldown("ChequeMoney", 3);
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
