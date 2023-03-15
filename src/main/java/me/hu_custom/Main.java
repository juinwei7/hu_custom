package me.hu_custom;

import me.hu_custom.command.Item_get;
import me.hu_custom.command.SlimeChunk;
import me.hu_custom.features.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance = null;

    public static boolean eggspawner_on,PiglinDorp_on,Resource_on,SlimeChunk_on,new_player_perr_on,clock_enabled;
    public static String Resource_urlt,new_player_perr_st,prefix,new_player_perr;
    public static String clock_right, clock_shiftRight, clock_left, clock_shiftLeft;


    @Override
    public void onEnable() {
        instance = this;
        FileConfiguration config = getConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        System.out.println("hu_cou 啟動成功");



        getServer().getPluginManager().registerEvents(new egg_spawner(), this);
        getServer().getPluginManager().registerEvents(new Piglin_Dorp(),this);
        getServer().getPluginManager().registerEvents(new Resource(),this);
        getServer().getPluginManager().registerEvents(new new_player(),this);
        getServer().getPluginManager().registerEvents(new Click_clock(),this);



        eggspawner_on = config.getBoolean("egg_spawner-on");
        PiglinDorp_on = config.getBoolean("Piglin_Dorp-on");
        Resource_on = config.getBoolean("Resource-on");
        SlimeChunk_on = config.getBoolean("SlimeChunk-on");
        new_player_perr_on = config.getBoolean("new_player_perr-on");
        clock_enabled = config.getBoolean("clock_enabled");


        prefix = config.getString("prefix");
        Resource_urlt = config.getString("Resource-urlt");
        new_player_perr_st = config.getString("new_player_perr-st");
        new_player_perr = config.getString("new_player_perr");

        clock_right = config.getString("clock_right");
        clock_shiftRight = config.getString("clock_shift_right");
        clock_left = config.getString("clock_left");
        clock_shiftLeft = config.getString("clock_shift_left");


        getCommand("urlt").setExecutor(new me.hu_custom.command.Resource());
        getCommand("smp").setExecutor(new SlimeChunk());
        getCommand("huget").setExecutor(new Item_get());


    }

    @Override
    public void onDisable() {
        System.out.println("hu_cou 關閉");
    }



}
