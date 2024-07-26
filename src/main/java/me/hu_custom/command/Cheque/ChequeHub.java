package me.hu_custom.command.Cheque;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.DataBase.DataBase;
import me.hu_custom.Main;
import me.hu_custom.util.Config;
import me.hu_custom.util.cooldown;
import me.hu_custom.util.log;
import org.black_ixx.playerpoints.PlayerPoints;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.lumine.mythic.core.utils.RandomUtil.random;

public class ChequeHub implements CommandExecutor, Listener {

    static String database_name = "chequehub";

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
        UUID uuid = player.getUniqueId();
        String noperrPermission = Config.getConfig().getString(Config.ChequeHub_perr);

        if (lable.equalsIgnoreCase("chequehub")) {
            if(!Config.isChequeHubenabled()){
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_noperr));
                return false;
            }
            if (noperrPermission != null && player.hasPermission(noperrPermission)) {
                int sendhub = 0; //輸入金額
                int points = PlayerPoints.getInstance().getAPI().look(uuid);

                if (args.length == 0) {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_tointerror));
                    return true;
                }
                if (args.length == 1) {
                    try {
                        sendhub = Integer.parseInt(args[0]);
                        // 現在你可以使用整數變數 secondArg 來處理第二個參數
                    } catch (NumberFormatException e) {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_tointerror));
                        return false;
                    }
                    if (sendhub%10!=0 || sendhub<100 || sendhub>5000){ //確認須為10的倍數
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_is_10multiple));
                        return false;
                    }
                    if (player.getInventory().firstEmpty() == -1) { // 背包已满，取消添加物品的操作
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_bag_max));
                        return false;
                    }
                    double sendhub_tax = sendhub*1.10; //x稅率 10%
                    if(sendhub_tax <= points){ //檢查hub是否足夠

                        String Identification = generateRandomString(10);
                        Calendar calendar = Calendar.getInstance();
                        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

                        PlayerPoints.getInstance().getAPI().take(uuid, (int) sendhub_tax);
                        ItemStack itemStack = new ItemStack(hub_item_paper(player,sendhub,Identification)); //製作支票(含辨識碼8位數)
                        player.getInventory().addItem(itemStack);
                        DataBase.chequesaveData(database_name,Identification,player.getName(), String.valueOf(sendhub),timestamp);
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + "§a成功創建支票，金額 $" + sendhub);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeHub_discordnb) + " :blue_square: 創建幻幣支票 " + player.getName() + ", " + sendhub; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                    }else {
                        player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_nomoney));
                    }

                }else {
                    player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_tointerror));
                }

            }else {
                player.sendMessage(Config.getConfig().getString(Config.prefix) + Config.getConfig().getString(Config.ChequeHub_message_noperr));
            }
        }

        return false;
    }

    private ItemStack hub_item_paper(Player player,int value,String Identification) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:00");
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, 8); //時間延遲
        Date expirationTime = calendar.getTime();


        List<String> itemlore = Config.getChequeHubitemList();

        String currentTimeStr = sdf.format(currentTime);
        String expirationTimeStr = sdf.format(expirationTime);
        String value_String = String.valueOf(value);

        List<String> updatedLore = new ArrayList<>();
        for (String loreLine : itemlore) {
            loreLine = loreLine.replace("%player%", player.getName());
            loreLine = loreLine.replace("%time%", currentTimeStr);
            loreLine = loreLine.replace("%over_time%", expirationTimeStr);
            loreLine = loreLine.replace("%hub_value%",value_String);
            loreLine = loreLine.replace("%hub_number%",Identification);
            updatedLore.add(loreLine);
        }


        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Config.getConfig().getString(Config.ChequeHub_chequeitem_name).replace("%hub_value%",value_String));
        itemMeta.setLore(updatedLore);
        itemStack.setItemMeta(itemMeta);


        //////////////// NBT /////////////////////
        String player_id = player.getName();
        String uuid = String.valueOf(player.getUniqueId());

        NBTItem nbt1 = new NBTItem(itemStack);
        nbt1.setString("hu_ItemName","ChequeHub");
        nbt1.setInteger("CustomModelData", 2000);
        nbt1.setInteger("hub_value", value);
        nbt1.setString("hub_player", player_id);
        nbt1.setString("hub_uuid", uuid);
        nbt1.setString("hub_identification", Identification);



        log.log("創建金額 " + value_String + ", " + player.getName() ,"ChequeHub");

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
                if (nbti.hasKey("hu_ItemName") && nbti.getString("hu_ItemName").equals("ChequeHub")) {
                    if (!cooldown.isOnCooldown("ChequeHub")) {

                        /*
                          處理大額支票 辨識碼
                         */
                        int value = nbti.getInteger("hub_value");
                        String hub_player = nbti.getString("hub_player");
                        if (nbti.hasTag("hub_identification")){
                            String identification = nbti.getString("hub_identification");
                            if (DataBase.chequeBooldean(database_name,identification)){
                                String name_use = DataBase.chequeloadData(database_name,"name_use",identification);
                                if (name_use!=null){
                                    player.sendMessage("§f該支票已被使用，使用玩家 " + name_use);
                                    String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeHub_discordnb) + " :rotating_light: 正在嘗試使用用過的支票 " + player.getName() + ", 辨識碼" + identification; // 替换为你想要执行的命令
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                                    cooldown.setCooldown("ChequeHub", 4);
                                    return;
                                }else {
                                    Calendar calendar = Calendar.getInstance();
                                    Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                                    DataBase.chequesaveData(database_name,identification,player.getName(), player.getName(),timestamp);
                                }
                            }else {
                                player.sendMessage("§c該支票無創建紀錄，請截圖並開客服單詢問");
                                String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeHub_discordnb) + " :rotating_light: 正在嘗試使用未知的支票 " + player.getName() + ", 辨識碼" + identification; // 替换为你想要执行的命令
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                                cooldown.setCooldown("ChequeHub", 4);
                                return;
                            }
                        }

                        //開始正常執行

                        item.setAmount(item.getAmount() - 1);

                        int points = PlayerPoints.getInstance().getAPI().look(player.getUniqueId());
                        PlayerPoints.getInstance().getAPI().give(player.getUniqueId(), value);

                        int points_ans = PlayerPoints.getInstance().getAPI().look(player.getUniqueId());

                        log.log("核銷金額 " + value + ", " + player.getName() + " ， 生成者: " + hub_player, "ChequeHub");

                        player.sendMessage("§7============================");
                        player.sendMessage(Config.getConfig().getString(Config.ChequeHub_message_success));
                        player.sendMessage("提取前擁有: ✡" + points);
                        player.sendMessage("提取後擁有: ✡" + points_ans);
                        player.sendMessage("");
                        player.sendMessage("§c若領取失敗，請開客服單並提供截圖。");
                        player.sendMessage("§7=============================");
                        cooldown.setCooldown("ChequeHub", 5);
                        String commandToExecute = "discordsrv broadcast #" + Config.getConfig().getString(Config.ChequeHub_discordnb) + " :red_square: 使用幻幣支票 " + player.getName() + ", " + value; // 替换为你想要执行的命令
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                    }else {
                        cooldown.sendActionBar(player,Config.getConfig().getString(Config.ChequeHub_message_cooldown));
                    }
                }
            }
        }
    }
}
