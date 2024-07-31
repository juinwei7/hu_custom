package me.hu_custom;

import de.Linus122.SafariNet.API.SafariNet;
import lombok.Getter;
import me.hu_custom.DataBase.DataBase;
import me.hu_custom.Hook.PlaceholderUtil;
import me.hu_custom.command.*;
import me.hu_custom.command.Cheque.ChequeExp;
import me.hu_custom.command.Cheque.ChequeHub;
import me.hu_custom.command.Cheque.ChequeMoney;
import me.hu_custom.features.*;
import me.hu_custom.features.Resource;
import me.hu_custom.features.Safari.SafariNetListener;
import me.hu_custom.util.Buff;
import me.hu_custom.util.Cmd_PlayeyList;
import me.hu_custom.util.Config;
import me.hu_custom.util.WarpItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public final class Main extends JavaPlugin {

    private BukkitTask totemTask;

    public static DataBase dataBase = null;

    public static Main instance = null;
    @Getter
    private static Main inst;

    @Getter
    public static PlaceholderUtil papi;

    @Getter
    private static boolean placeholder;

    public static Economy econ = null;


    @Override
    public void onEnable() {
        inst = this;
        instance = this;
        Config.loadConfig();
        WarpItem.loadWarpitem();
        Buff.loadBuff();
        Cmd_PlayeyList.loadPlayer_List();

        dataBase = new DataBase(getConfig());
        System.out.println("hu_cou 啟動成功");

        startTotemTask(); //加載疊加不死圖騰

        if (hasplugin("Residence")) {
            SafariNetListener safarinetListener = new SafariNetListener();
            SafariNet.addListener(safarinetListener);
            getServer().getPluginManager().registerEvents(new Rule(), this);
        }else {
            Bukkit.getLogger().info("未檢測到Residence，不加載相關功能");
        }


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
        getServer().getPluginManager().registerEvents(new ChequeHub(), this);
        getServer().getPluginManager().registerEvents(new SHIFT_F(), this);
        getServer().getPluginManager().registerEvents(new EXP_LIMIT(), this);
        getServer().getPluginManager().registerEvents(new TaxCommand(), this);
        getServer().getPluginManager().registerEvents(new warpitem(), this);
        getServer().getPluginManager().registerEvents(new Player_Fly(), this);
        getServer().getPluginManager().registerEvents(new NO_Drop(inst), this);

        getServer().getPluginManager().registerEvents(new check_perr(), this);
        getServer().getPluginManager().registerEvents(new BossTime(), this);
        getServer().getPluginManager().registerEvents(new BuffCommand(), this);
        getServer().getPluginManager().registerEvents(new cmd_playerlist(), this);
        getServer().getPluginManager().registerEvents(new sx_bag_perr(), this);
        getServer().getPluginManager().registerEvents(new Get_Drop(), this);
        getServer().getPluginManager().registerEvents(new Totem_Overlay(), this);




        getCommand("equipment_roll").setExecutor(new Equipment_rollCommand());
        getCommand("urlt").setExecutor(new me.hu_custom.command.Resource());
        getCommand("smp").setExecutor(new SlimeChunk());
        getCommand("huget").setExecutor(new Item_get());
        getCommand("mry").setExecutor(new marry());
        getCommand("chequemoney").setExecutor(new ChequeMoney());
        getCommand("chequeexp").setExecutor(new ChequeExp());
        getCommand("chequehub").setExecutor(new ChequeHub());
        getCommand("bind").setExecutor(new Bind());
        getCommand("unbind").setExecutor(new Bind());
        getCommand("dr").setExecutor(new NO_Drop(inst));
        getCommand("bosstime").setExecutor(new BossTime());
        getCommand("sxbagkey").setExecutor(new SxbagkeyCommand());
        getCommand("taxcheck").setExecutor(new TaxCommand());
        getCommand("buffmenu").setExecutor(new BuffCommand());
        getCommand("cmd_playerlist").setExecutor(new cmd_playerlist());
        getCommand("autosell").setExecutor(new Get_Drop());

        if (!setupEconomy()) {
            getLogger().severe("Vault插件未找到，禁用插件！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // 注册PlaceholderAPI扩展
            new PlaceholderUtil().register();
        } else {
            // PlaceholderAPI未找到，可以选择在这里处理
            getLogger().warning("PlaceholderAPI not found, some features will be disabled!");
        }


    }


    @Override
    public void onDisable() {
        if (totemTask != null && !totemTask.isCancelled()){
            totemTask.cancel();
        }
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

    private boolean hasplugin(String plugin) {
        return getServer().getPluginManager().getPlugin(plugin) != null;
    }

    private void startTotemTask() { //不死圖藤
        totemTask = new Totem_Overlay.TotemTask().runTaskTimer(this, 0, 1L);
    }

}
