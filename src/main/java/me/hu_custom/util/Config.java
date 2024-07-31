package me.hu_custom.util;

import jdk.javadoc.internal.tool.Main;
import lombok.Getter;
import me.hu_custom.command.Cheque.ChequeExp;
import me.hu_custom.command.Cheque.ChequeHub;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class Config {

    public static final String prefix = "prefix";

    public static final String Lobby_perr = "Lobby_perr";
    public static final String new_player_perr_on = "new_player_perr-on";
    public static final String new_player_perr = "new_player_perr";
    public static final String new_player_perr_st = "new_player_perr-st";
    public static final String limit_event = "limit_event.enabled";
    public static final String egg_spawner = "limit_event.egg_spawner";
    public static final String Piglin_Dorp = "limit_event.Piglin_Dorp";
    public static final String Slime_Chunk = "limit_event.Slime_Chunk";
    public static final String Resource_enabled = "Resource.enabled";
    public static final String Resource_urlt = "Resource.urlt";
    public static final String clock_enabled = "clock.enabled";
    public static final String clock_right = "clock.right";
    public static final String clock_shift_right = "clock.shift_right";
    public static final String clock_left = "clock.left";
    public static final String clock_shift_left = "clock.shift_left";

    public static final String smithing_table_message = "Prevent.smithing_table_message";
    public static final String Map_BindStatus = "Prevent.Map_BindStatus";
    public static final String Book_BindStatus = "Prevent.Book_BindStatus";

    public static final String BIND_Settings_value= "BIND.Settings.value";
    public static final String BIND_Settings_BindLore1 = "BIND.Settings.BindLore1";
    public static final String BIND_Messages_NoMoney= "BIND.Messages.NoMoney";
    public static final String BIND_Messages_AlreadyBinded = "BIND.Messages.AlreadyBinded";
    public static final String BIND_Messages_BindSuccessfully = "BIND.Messages.BindSuccessfully";
    public static final String BIND_Messages_NotBinded = "BIND.Messages.NotBinded";
    public static final String BIND_Messages_MainHandIsAir = "BIND.Messages.MainHandIsAir";
    public static final String BIND_Messages_UnbindSuccessfully = "BIND.Messages.UnbindSuccessfully";

    //裝備卷兌換
    public static final String Equipment_enabled = "Equipment.enabled";
    public static final String Equipment_Gui_name = "Equipment.Gui_name";
    public static final String Equipment_Messages_NoNbtTag = "Equipment.Messages.NoNbtTag";
    public static final String Equipment_Messages_NOITEM = "Equipment.Messages.NOITEM";
    public static final String Equipment_Messages_NOAmount = "Equipment.Messages.NOAmount";
    public static final String Equipment_Equipment_list = "Equipment.Equipment_list";

    //tax扣稅
    public static final String TAX_enabled_world = "TAX.enabled_world";
    public static final String TAX_money_list = "TAX.money_list";

    //boss冷卻時間
    public static final String BossTimeing_enabled = "BossTimeing.enabled";
    public static final String BossTimeing_SpawnerName_list = "BossTimeing.SpawnerName";
    public static final String BossTimeing_TimeCooldown_list = "BossTimeing.TimeCooldown";



    public static final String COMMAND = "COMMAND";

    //支票系統
    public static final String ChequeMoney_enabled = "ChequeMoney.enabled";
    public static final String ChequeMoney_discordnb = "ChequeMoney.discordnb";
    public static final String ChequeMoney_perr = "ChequeMoney.Cheque_perr";
    public static final String ChequeMoney_message_noperr = "ChequeMoney.message.noperr";
    public static final String ChequeMoney_message_tointerror = "ChequeMoney.message.tointerror";
    public static final String ChequeMoney_message_nomoney = "ChequeMoney.message.nomoney";
    public static final String ChequeMoney_message_success = "ChequeMoney.message.success";
    public static final String ChequeMoney_message_cooldown = "ChequeMoney.message.cooldown";
    public static final String ChequeMoney_chequeitem_name = "ChequeMoney.chequeitem.name";
    public static final String ChequeMoney_chequeitem_lore = "ChequeMoney.chequeitem.lore";

    public static final String ChequeExp_enabled = "ChequeExp.enabled";
    public static final String ChequeExp_discordnb = "ChequeExp.discordnb";
    public static final String ChequeExp_perr = "ChequeExp.Cheque_perr";
    public static final String ChequeExp_message_noperr = "ChequeExp.message.noperr";
    public static final String ChequeExp_message_tointerror = "ChequeExp.message.tointerror";
    public static final String ChequeExp_message_nomoney = "ChequeExp.message.nomoney";
    public static final String ChequeExp_message_success = "ChequeExp.message.success";
    public static final String ChequeExp_message_cooldown = "ChequeExp.message.cooldown";
    public static final String ChequeExp_chequeitem_name = "ChequeExp.chequeitem.name";
    public static final String ChequeExp_chequeitem_lore = "ChequeExp.chequeitem.lore";

    public static final String ChequeHub_enabled = "ChequeHub.enabled";
    public static final String ChequeHub_discordnb = "ChequeHub.discordnb";
    public static final String ChequeHub_perr = "ChequeHub.Cheque_perr";
    public static final String ChequeHub_message_noperr = "ChequeHub.message.noperr";
    public static final String ChequeHub_message_tointerror = "ChequeHub.message.tointerror";
    public static final String ChequeHub_message_is_10multiple = "ChequeHub.message.is_10multiple";
    public static final String ChequeHub_message_nomoney = "ChequeHub.message.nomoney";
    public static final String ChequeHub_message_bag_max = "ChequeHub.message.bag_max";
    public static final String ChequeHub_message_success = "ChequeHub.message.success";
    public static final String ChequeHub_message_cooldown = "ChequeHub.message.cooldown";
    public static final String ChequeHub_chequeitem_name = "ChequeHub.chequeitem.name";
    public static final String ChequeHub_chequeitem_lore = "ChequeHub.chequeitem.lore";

    //堆疊的不死圖藤
    public static final String Totem_Overlay_nbt = "Totem_Overlay.nbt";
    public static final String Totem_Overlay_name = "Totem_Overlay.name";
    public static final String Totem_Overlay_model = "Totem_Overlay.model";
    public static final String Totem_Overlay_lore = "Totem_Overlay.lore";



    @Getter
    private static YamlConfiguration config;

    @Getter
    private static List<String> COMMANDList;
    @Getter
    private static List<String> ChequeMoneyitemList;
    @Getter
    private static List<String> ChequeExpitemList;
    @Getter
    private static List<String> ChequeHubitemList;

    @Getter
    private static List<String> Equipment_list;

    @Getter
    private static List<String> TAX_moneylist;
    @Getter
    private static List<String> BossTimeing_SpawnerName;
    @Getter
    private static List<String> BossTimeing_TimeCooldown;

    @Getter
    private static List<String> Totem_Overlay_lore_list;

    @Getter
    private static boolean newplayerperr_on;

    @Getter
    private static boolean limitevent;
    @Getter
    private static boolean eggspawner;
    @Getter
    private static boolean PiglinDorp;
    @Getter
    private static boolean SlimeChunk;
    @Getter
    private static boolean Resourceenabled;
    @Getter
    private static boolean clock;
    @Getter
    private static boolean ChequeMoneyenabled;
    @Getter
    private static boolean ChequeExpenabled;
    @Getter
    private static boolean ChequeHubenabled;
    @Getter
    private static boolean Equipmentenabled;



    public static void loadConfig() {
        File file = new File(me.hu_custom.Main.getInst().getDataFolder(), "Config.yml");
        if (!file.exists()) {
            me.hu_custom.Main.getInst().getLogger().info("Create Config.yml");
            me.hu_custom.Main.getInst().saveResource("Config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(file);
        newplayerperr_on = config.getBoolean(new_player_perr_on);
        limitevent = config.getBoolean(limit_event);
        eggspawner = config.getBoolean(egg_spawner);
        PiglinDorp = config.getBoolean(Piglin_Dorp);
        SlimeChunk = config.getBoolean(Slime_Chunk);
        Resourceenabled = config.getBoolean(Resource_enabled);
        clock = config.getBoolean(clock_enabled);
        ChequeMoneyenabled = config.getBoolean(ChequeMoney_enabled);
        ChequeExpenabled = config.getBoolean(ChequeExp_enabled);
        ChequeHubenabled = config.getBoolean(ChequeHub_enabled);
        Equipmentenabled = config.getBoolean(Equipment_enabled);

        COMMANDList = config.getStringList(COMMAND);
        ChequeMoneyitemList = config.getStringList(ChequeMoney_chequeitem_lore);
        ChequeExpitemList = config.getStringList(ChequeExp_chequeitem_lore);
        ChequeHubitemList = config.getStringList(ChequeHub_chequeitem_lore);
        Equipment_list = config.getStringList(Equipment_Equipment_list);
        TAX_moneylist = config.getStringList(TAX_money_list);
        BossTimeing_TimeCooldown = config.getStringList(BossTimeing_TimeCooldown_list);
        BossTimeing_SpawnerName = config.getStringList(BossTimeing_SpawnerName_list);
        Totem_Overlay_lore_list = config.getStringList(Totem_Overlay_lore);
    }
}
