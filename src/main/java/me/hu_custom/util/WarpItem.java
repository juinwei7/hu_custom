package me.hu_custom.util;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class WarpItem {

    public static final String warp_name_list = "warp_name.list";
    public static final String item_name_list = "item_name.list";

    @Getter
    private static YamlConfiguration warpitem;

    @Getter
    private static List<String> warp_namelist;

    @Getter
    private static List<String> item_namelist;

    public static void loadWarpitem() {
        File file = new File(me.hu_custom.Main.getInst().getDataFolder(), "WarpItem.yml");
        if (!file.exists()) {
            me.hu_custom.Main.getInst().getLogger().info("Create WarpItem.yml");
            me.hu_custom.Main.getInst().saveResource("WarpItem.yml", true);
        }
        warpitem = YamlConfiguration.loadConfiguration(file);
        
        warp_namelist = warpitem.getStringList(warp_name_list);
        item_namelist = warpitem.getStringList(item_name_list);


       // COMMANDList = config.getStringList(COMMAND);

    }
}
