package me.hu_custom.util;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Cmd_PlayeyList {

    public static final String command = "command";

    public static final String player_list = "player_list";

    @Getter
    private static YamlConfiguration cmd_playeyList;

    @Getter
    private static List<String> player_list_ls;


    public static void loadPlayer_List() {
        File file = new File(me.hu_custom.Main.getInst().getDataFolder(), "Cmd_PlayerList.yml");
        if (!file.exists()) {
            me.hu_custom.Main.getInst().getLogger().info("Create Cmd_PlayerList.yml");
            me.hu_custom.Main.getInst().saveResource("Cmd_PlayerList.yml", true);
        }
        cmd_playeyList = YamlConfiguration.loadConfiguration(file);

        player_list_ls = cmd_playeyList.getStringList(player_list);

    }
}
