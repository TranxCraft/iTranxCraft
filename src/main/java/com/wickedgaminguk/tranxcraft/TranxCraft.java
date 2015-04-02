package com.wickedgaminguk.tranxcraft;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.bridge.ipboard.IpBoard;
import com.wickedgaminguk.tranxcraft.commands.Command_tranxcraft;
import com.wickedgaminguk.tranxcraft.listeners.ListenerLoader;
import com.wickedgaminguk.tranxcraft.listeners.PlayerListener;
import com.wickedgaminguk.tranxcraft.modules.AnnouncementModule;
import com.wickedgaminguk.tranxcraft.modules.MailModule;
import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.modules.TwitterModule;
import com.wickedgaminguk.tranxcraft.modules.WarnModule;
import com.wickedgaminguk.tranxcraft.player.AdminManager;
import com.wickedgaminguk.tranxcraft.player.BanManager;
import com.wickedgaminguk.tranxcraft.player.PlayerManager;
import com.wickedgaminguk.tranxcraft.player.RewardBot;
import com.wickedgaminguk.tranxcraft.player.RewardWorker;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StatisticManager;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.UtilManager;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.io.File;
import java.util.logging.Level;

public class TranxCraft extends BukkitPlugin {

    public TranxCraft plugin;
    public YamlConfig config;
    public BukkitCommandHandler handler;
    public MySQL database;
    public ListenerLoader listenerLoader;
    public ModuleLoader modLoader;
    public DebugUtils debugUtils;
    public SqlModule sqlModule;
    public WarnModule warnModule;
    public MailModule mailModule;
    public TwitterModule twitterModule;
    public AdminManager adminManager;
    public PlayerManager playerManager;
    public BanManager banManager;
    public UtilManager utilManager;
    public IpBoard ipBoard;

    @Override
    public void onLoad() {
        plugin = this;
        config = new YamlConfig(plugin, "config.yml");
        handler = new BukkitCommandHandler(plugin);
        utilManager = new UtilManager(plugin);
    }

    @Override
    public void onEnable() {
        BukkitLib.init(plugin);

        config.load();

        if (!ValidationUtils.isValidSql(config)) {
            LoggerUtils.severe(plugin, "Invalid MySQL configuration found. This plugin will now disable.");
            Bukkit.getPluginManager().disablePlugin(plugin);

            return;
        }

        database = new MySQL(config.getString("mysql.hostname"), config.getString("mysql.port"), config.getString("mysql.username"), config.getString("mysql.password"), config.getString("mysql.database"));
        database.openConnection();

        if (database.isOpen()) {
            LoggerUtils.info(plugin, "Database connection opened.");
        }
        else {
            LoggerUtils.severe(plugin, "Database failed to open. This plugin will now disable.");
            Bukkit.getPluginManager().disablePlugin(plugin);

            return;
        }

        handler.setCommandLocation(Command_tranxcraft.class.getPackage());

        listenerLoader = new ListenerLoader(plugin);
        modLoader = new ModuleLoader(plugin);
        
        adminManager = new AdminManager(plugin);
        playerManager = new PlayerManager(plugin);
        
        modLoader.loadModules(SqlModule.class.getPackage());
        sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");

        File externalModules = new File(plugin.getDataFolder() + "/modules");

        if (!externalModules.exists()) {
            externalModules.mkdirs();
        }

        modLoader.loadModules(externalModules.listFiles());

        listenerLoader.loadListeners(PlayerListener.class.getPackage());

        if (database.isOpen()) {
            if (!sqlModule.isInitialized()) {
                LoggerUtils.info(plugin, "Initializing database.");
                sqlModule.initialize();
                LoggerUtils.info(plugin, "Database initialized.");
            }
        }
        
        warnModule = (WarnModule) ModuleLoader.getModule("WarnModule");
        mailModule = (MailModule) ModuleLoader.getModule("MailModule");
        twitterModule = (TwitterModule) ModuleLoader.getModule("TwitterModule");

        banManager = new BanManager(plugin);

        DebugUtils.setEnabled(Boolean.valueOf(sqlModule.getConfigEntry("log_enabled")));
        DebugUtils.setLevel(Level.parse(sqlModule.getConfigEntry("log_level").toUpperCase()));
        DebugUtils.setDebugLevel(Integer.valueOf(sqlModule.getConfigEntry("log_debug_level")));

        if (!ValidationUtils.isValidEmailConfig(sqlModule)) {
            LoggerUtils.warning(plugin, "Invalid Mail configuration found. Mail functionality will be disabled.");
            mailModule.setEnabled(false);
        }

        int rewardDelay = (int) ((Math.random() * (7200 - 2700)) + 2700);

        new RewardBot(plugin).runTaskTimer(plugin, rewardDelay * 20L, rewardDelay * 20L);

        //This is for every Minecraft week, if you're wondering about the 8400 seconds/140 minutes. Somewhat resembles a real-life wage without being OTT (like, monthly).
        new RewardWorker(plugin).runTaskTimer(plugin, 8400 * 20L, 8400 * 20L);

        new StatisticManager(plugin).runTaskTimerAsynchronously(plugin, 30 * 20L, 30 * 20L);

        ipBoard = new IpBoard(plugin);
        
        LoggerUtils.info(plugin, StrUtils.concatenate("Loaded ", ModuleLoader.getModuleCount(), " modules."));
        LoggerUtils.info(plugin, StrUtils.concatenate("Loaded ", ListenerLoader.getListenerCount(), " listeners."));
        LoggerUtils.info(plugin, StrUtils.concatenate("Loaded ", AnnouncementModule.getAnnouncementCount(), " announcements."));
        LoggerUtils.info(plugin, StrUtils.concatenate("Loaded ", banManager.loadCache(), " bans."));
        LoggerUtils.info(plugin, StrUtils.concatenate("Loaded ", adminManager.loadCache(), " admins."));
    }

    @Override
    public void onDisable() {
        if (database.isOpen()) {
            database.closeConnection();
        }
        
        config.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return handler.handleCommand(sender, cmd, commandLabel, args);
    }
}
