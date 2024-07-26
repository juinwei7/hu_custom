package me.hu_custom.command;

import de.tr7zw.nbtapi.NBTItem;
import me.hu_custom.Main;
import me.hu_custom.util.Buff;
import me.hu_custom.util.Luckperms_hook;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.print.attribute.standard.NumberUp;
import java.util.ArrayList;
import java.util.List;

import static me.hu_custom.Main.econ;
import static me.hu_custom.util.Buff.vip_discount;

public class BuffCommand implements CommandExecutor, Listener {

    static String GUINAME = Buff.getBuff().getString(Buff.GUINAME).replaceAll("&", "§");

    private static Inventory addgui() {
        Inventory inv = Bukkit.createInventory(null, 54, GUINAME);
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE); //0-9
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        for (int i=0;i<9;i++) {if (i != 4) {inv.setItem(i, itemStack);}}

        ItemStack item_4 = new ItemStack(Material.ENDER_EYE); //主格
        ItemMeta meta_4 = item_4.getItemMeta();
        meta_4.setDisplayName(Buff.getBuff().getString(Buff.BUFF_ITEM_NAME));
        meta_4.setLore(Buff.getBUFF_ITEM_LORE_LIST());
        item_4.setItemMeta(meta_4);
        inv.setItem(4,item_4);
        return inv;
    }

    public static ItemStack createItem(Material material, String name, int time, int money, int setCustomModelData, String bufftype, int bufflevel,double vip,boolean is_SUB) {
        ItemStack item = new ItemStack(material);

        List<String> lore = new ArrayList<>();
        if (!is_SUB) {
            if (vip == 1) {
                lore.add(" ");
                lore.add("§7加成時間: " + time + " 秒");
                lore.add("§7需要金錢: §a" + money + "§7 $");
            } else {
                lore.add(" ");
                lore.add("§7加成時間: " + time + " 秒");
                lore.add("§7需要金錢: §c§m" + money + "§7 $");
                lore.add(" ");
                lore.add("§7優惠金額: §a" + money * vip);
            }
        }else {
            if (vip == 1) {
                lore.add(" ");
                lore.add("§7訂閱天數: " + time + " 天");
                lore.add("§7需要金錢: §a" + money + "§7 $");
            } else {
                lore.add(" ");
                lore.add("§7訂閱天數: " + time + " 天");
                lore.add("§7需要金錢: §c§m" + money + "§7 $");
                lore.add(" ");
                lore.add("§7優惠金額: §a" + money * vip);
            }
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§f✦ " + name + " ✦");
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(setCustomModelData);
        item.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("buff_type", bufftype);
        nbtItem.setInteger("buff_time", time);
        nbtItem.setInteger("buff_level", bufflevel);
        nbtItem.setDouble("buff_money", money*vip);

        if (is_SUB){nbtItem.setInteger("buff_sub", 1);}

        return nbtItem.getItem();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String perr = Buff.getBuff().getString(Buff.PERR);
            assert perr != null;

            if (player.hasPermission(perr)) {
                player.openInventory(addgui());
            }else {player.sendMessage("§f〔系統〕§r§c該分流不開放使用BUFF加成。");}
        }
        return false;
    }

    @EventHandler
    void OnOpen(InventoryOpenEvent event) {
        if (event.getView().getTitle().equals(GUINAME)) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            double vip_discount = vip_discount("DEFAULT"); //vip優惠
            if (player.hasPermission("hu_building.VIP5")){vip_discount = vip_discount("VIP5");}
            if (player.hasPermission("hu_building.VIP6")){vip_discount = vip_discount("VIP6");}
            if (player.hasPermission("hu_building.VIP7")){vip_discount = vip_discount("VIP7");}
            if (player.hasPermission("hu_building.VIP8")){vip_discount = vip_discount("VIP8");}

            List<String> bufflist = Buff.getBUFF_list();
            int size = 9;
            int size_sub = 45;
            for (String line : bufflist) {
                String[] parts = line.split(",");
                Material material = Material.valueOf(parts[0].toUpperCase());
                int model = Integer.parseInt(parts[1]);
                String buffname = parts[2];
                String buff = parts[3];
                int level = Integer.parseInt(parts[4]);
                int time = Integer.parseInt(parts[5]);
                int money = Integer.parseInt(parts[6]);
                if (size < 45) {
                    ItemStack item = createItem(material, buffname, time, money, model, buff, level,vip_discount,false);
                    inv.setItem(size, item);
                    size++;
                }
            }

            List<String> BUFF_SUB_LIST = Buff.getBUFF_SUB_list();
            for (String line : BUFF_SUB_LIST) {
                String[] parts = line.split(",");
                Material material = Material.valueOf(parts[0].toUpperCase());
                int model = Integer.parseInt(parts[1]);
                String buffname = parts[2];
                String buff = parts[3];
                int level = Integer.parseInt(parts[4]);
                int time_day = Integer.parseInt(parts[5]);
                int money = Integer.parseInt(parts[6]);
                if (size_sub < 54) {
                    ItemStack item = createItem(material, buffname, time_day, money, model, buff, level,vip_discount,true);
                    inv.setItem(size_sub, item);
                    size_sub++;
                }
            }
        }
    }
    @EventHandler
    void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(GUINAME)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if (item==null){return;}

            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasTag("buff_type")) {
                String buff_type = nbtItem.getString("buff_type");
                int buff_level = nbtItem.getInteger("buff_level");
                int buff_time = nbtItem.getInteger("buff_time");
                double buff_money = nbtItem.getDouble("buff_money");
                double player_money = econ.getBalance(player);
                if (player_money >= buff_money) {
                    econ.withdrawPlayer(player, buff_money);
                    if (nbtItem.hasTag("buff_sub") && nbtItem.getInteger("buff_sub").equals(1)){
                        String buff_sub = "buff.use." + buff_type;
                        give_eff(player, buff_type, buff_time*60*10, buff_level); //給120分鐘
                        player.sendMessage("§f〔系統〕§r§a已訂閱BUFF效果，花費 " + buff_money + " $");
                        player.sendMessage("§f〔系統〕§r§a本次共訂閱 "+ buff_time +" 天");
                        Luckperms_hook.addperr(player,buff_sub,288);
                        return;
                    }

                    give_eff(player, buff_type, buff_time, buff_level);
                    player.sendMessage("§f〔系統〕§r§a已獲得BUFF效果，花費 " + buff_money + " $");
                } else {
                    player.sendMessage("§f〔系統〕§r§c遊戲幣不足，您需要擁有 " + buff_money + " $");
                }
            }

        }
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                boolean openbuff = false;
                List<String> buff_list = new ArrayList<>();
                buff_list.add("FAST_DIGGING");
                buff_list.add("WATER_BREATHING");
                buff_list.add("NIGHT_VISION");
                buff_list.add("GLOWING");


                for (String buff_line : buff_list) {
                    String buff_sub = "buff.use." + buff_line;
                    if (player.hasPermission(buff_sub)) {
                        give_eff(player, buff_line, 7200, 3);
                        openbuff = true;
                    }
                }
                if (openbuff) {
                    player.sendMessage("§f〔系統〕§r§a您訂閱的 BUFF 加成效果已開啟");
                }

            }
        }.runTaskLater(Main.instance, 40L);
    }


    private void give_eff(Player player,String effect, int time, int level){
        PotionEffect waterBreathing = new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()), time*20, level-1,true,false,true); // 6000 ticks = 5 minutes
        player.addPotionEffect(waterBreathing);

    }
}
