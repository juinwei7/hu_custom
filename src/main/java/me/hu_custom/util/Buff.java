package me.hu_custom.util;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Buff {

    public static final String GUINAME = "GUINAME";
    public static final String PERR = "PERMISSTONS.USE";

    public static final String BUFF_ITEM_NAME = "BUFF_ITEM.NAME";
    public static final String BUFF_ITEM_LORE = "BUFF_ITEM.LORE";

    public static final String BUFF = "BUFF";
    public static final String BUFF_SUB = "BUFF_SUB";


    @Getter
    private static YamlConfiguration buff;

    @Getter
    private static List<String> BUFF_list;
    @Getter
    private static List<String> BUFF_SUB_list;

    @Getter
    private static List<String> BUFF_ITEM_LORE_LIST;

    public static void loadBuff() {
        File file = new File(me.hu_custom.Main.getInst().getDataFolder(), "Buff.yml");
        if (!file.exists()) {
            me.hu_custom.Main.getInst().getLogger().info("Create Buff.yml");
            me.hu_custom.Main.getInst().saveResource("Buff.yml", true);
        }
        buff = YamlConfiguration.loadConfiguration(file);

        BUFF_list = buff.getStringList(BUFF);
        BUFF_SUB_list = buff.getStringList(BUFF_SUB);

        BUFF_ITEM_LORE_LIST = buff.getStringList(BUFF_ITEM_LORE);

    }

    public static double vip_discount(String VIP){return buff.getDouble("PERMISSTONS.VIP." + VIP);}

}
