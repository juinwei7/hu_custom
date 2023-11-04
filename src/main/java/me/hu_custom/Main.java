package me.hu_custom;

import lombok.Getter;
import me.hu_custom.command.Item_get;
import me.hu_custom.command.SlimeChunk;
import me.hu_custom.command.marry;
import me.hu_custom.features.*;
import me.hu_custom.util.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance = null;
    @Getter
    private static Main inst;


    @Override
    public void onEnable() {
        inst = this;
        instance = this;
        Config.loadConfig();
        System.out.println("hu_cou 啟動成功");



        getServer().getPluginManager().registerEvents(new egg_spawner(), this);
        getServer().getPluginManager().registerEvents(new Piglin_Dorp(),this);
        getServer().getPluginManager().registerEvents(new Resource(),this);
        getServer().getPluginManager().registerEvents(new new_player(),this);
        getServer().getPluginManager().registerEvents(new Click_clock(),this);
        //getServer().getPluginManager().registerEvents(new playerchat(),this);
        getServer().getPluginManager().registerEvents(new err_up_equipment(),this);
        getServer().getPluginManager().registerEvents(new EAT_consume(), this);





        getCommand("urlt").setExecutor(new me.hu_custom.command.Resource());
        getCommand("smp").setExecutor(new SlimeChunk());
        getCommand("huget").setExecutor(new Item_get());
        getCommand("mry").setExecutor(new marry());


    }

    @Override
    public void onDisable() {
        System.out.println("hu_cou 關閉");
    }



}
