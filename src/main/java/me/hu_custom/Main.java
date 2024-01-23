package me.hu_custom;

import lombok.Getter;
import me.hu_custom.DataBase.DataBase;
import me.hu_custom.command.*;
import me.hu_custom.command.Cheque.ChequeExp;
import me.hu_custom.command.Cheque.ChequeMoney;
import me.hu_custom.features.*;
import me.hu_custom.features.Resource;
import me.hu_custom.util.Config;
import me.hu_custom.util.WarpItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static DataBase dataBase = null;

    public static Main instance = null;
    @Getter
    private static Main inst;

    public static Economy econ = null;


    @Override
    public void onEnable() {
        inst = this;
        instance = this;
        Config.loadConfig();
        WarpItem.loadWarpitem();
        dataBase = new DataBase(getConfig());
        System.out.println("hu_cou 啟動成功");



        getServer().getPluginManager().registerEvents(new Equipment_rollCommand(), this);
        getServer().getPluginManager().registerEvents(new egg_spawner(), this);
        getServer().getPluginManager().registerEvents(new Piglin_Dorp(),this);
        getServer().getPluginManager().registerEvents(new Resource(),this);
        getServer().getPluginManager().registerEvents(new new_player(),this);
        getServer().getPluginManager().registerEvents(new Click_clock(),this);
        getServer().getPluginManager().registerEvents(new marry(), this);
        getServer().getPluginManager().registerEvents(new err_up_equipment(),this);
        getServer().getPluginManager().registerEvents(new EAT_consume(), this);
        getServer().getPluginManager().registerEvents(new NoPlace_haslore(), this);
        getServer().getPluginManager().registerEvents(new ChequeMoney(), this);
        getServer().getPluginManager().registerEvents(new ChequeExp(), this);
        getServer().getPluginManager().registerEvents(new SHIFT_F(), this);
        getServer().getPluginManager().registerEvents(new EXP_LIMIT(), this);
        getServer().getPluginManager().registerEvents(new TaxCommand(), this);
        getServer().getPluginManager().registerEvents(new warpitem(), this);
        getServer().getPluginManager().registerEvents(new Player_Fly(), this);
        getServer().getPluginManager().registerEvents(new NO_Drop(inst), this);




        getCommand("equipment_roll").setExecutor(new me.hu_custom.command.Equipment_rollCommand());
        getCommand("urlt").setExecutor(new me.hu_custom.command.Resource());
        getCommand("smp").setExecutor(new SlimeChunk());
        getCommand("huget").setExecutor(new Item_get());
        getCommand("mry").setExecutor(new marry());
        getCommand("chequemoney").setExecutor(new ChequeMoney());
        getCommand("chequeexp").setExecutor(new ChequeExp());
        getCommand("bind").setExecutor(new Bind());
        getCommand("unbind").setExecutor(new Bind());
        getCommand("dr").setExecutor(new NO_Drop(inst));
        getCommand("bosstime").setExecutor(new BossTime());
        getCommand("sxbagkey").setExecutor(new SxbagkeyCommand());
        getCommand("taxcheck").setExecutor(new TaxCommand());

        if (!setupEconomy()) {
            getLogger().severe("Vault插件未找到，禁用插件！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


    }


    @Override
    public void onDisable() {
        System.out.println("hu_cou 關閉");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }



}
