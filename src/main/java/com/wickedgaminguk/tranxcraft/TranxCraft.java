package com.wickedgaminguk.tranxcraft;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.commands.Command_tranxcraft;
import com.wickedgaminguk.tranxcraft.listeners.ListenerLoader;
import com.wickedgaminguk.tranxcraft.listeners.PlayerListener;
import com.wickedgaminguk.tranxcraft.modules.MailModule;
import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.modules.TwitterModule;
import com.wickedgaminguk.tranxcraft.modules.WarnModule;
import com.wickedgaminguk.tranxcraft.player.AdminManager;
import com.wickedgaminguk.tranxcraft.player.BanManager;
import com.wickedgaminguk.tranxcraft.player.PlayerManager;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.io.File;
import java.util.logging.Level;

public class TranxCraft extends BukkitPlugin {

    public static TranxCraft plugin;
    public YamlConfig config;
    public BukkitCommandHandler handler;
    public MySQL database;
    public ListenerLoader listenerLoader;
    public ModuleLoader modLoader;
    public BanManager banManager;
    public DebugUtils debugUtils;
    public SqlModule sqlModule;
    public WarnModule warnModule;
    private MailModule mailModule;
    public TwitterModule twitterModule;
    public AdminManager adminManager;
    public PlayerManager playerManager;

    @Override
    public void onLoad() {
        plugin = this;
        config = new YamlConfig(plugin, "config.yml");
        handler = new BukkitCommandHandler(plugin);
    }

    @Override
    public void onEnable() {
        BukkitLib.init(plugin);

        config.load();

        if (!ValidationUtils.isValidSql(config)) {
            LoggerUtils.severe(plugin, "Invalid MySQL configuration found. This plugin will now disable.");
            return;
        }

        database = new MySQL(config.getString("mysql.hostname"), config.getString("mysql.port"), config.getString("mysql.username"), config.getString("mysql.password"), config.getString("mysql.database"));
        database.openConnection();

        if (database.isOpen()) {
            LoggerUtils.info(plugin, "Database connection opened.");
        }
        else {
            LoggerUtils.info(plugin, "Database failed to open.");
            return;
        }

        handler.setCommandLocation(Command_tranxcraft.class.getPackage());

        listenerLoader = new ListenerLoader(plugin);
        modLoader = new ModuleLoader(plugin);
        
        adminManager = new AdminManager(plugin);
        playerManager = new PlayerManager(plugin);
        
        modLoader.loadModules(SqlModule.class.getPackage());
        modLoader.loadModules(new File(plugin.getDataFolder() + "/modules").listFiles());
        
        sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");

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
        debugUtils = new DebugUtils(Level.parse(sqlModule.getConfigEntry("loglevel").toUpperCase()));
        debugUtils.test();
        
        if (!ValidationUtils.isValidEmailConfig(sqlModule)) {
            LoggerUtils.warning(plugin, "Invalid Mail configuration found. Mail functionality will be disabled.");
            mailModule.setEnabled(false);
        }
        
        LoggerUtils.info(plugin, "Loaded " + ModuleLoader.getModuleCount() + " modules.");
        LoggerUtils.info(plugin, "Loaded " + banManager.loadCache() + " bans.");
        LoggerUtils.info(plugin, "Loaded " + adminManager.loadCache() + " admins.");
    }

    @Override
    public void onDisable() {
        if (database.isOpen()) {
            database.closeConnection();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return handler.handleCommand(sender, cmd, commandLabel, args);
    }
}
