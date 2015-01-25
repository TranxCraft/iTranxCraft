package com.wickedgaminguk.tranxcraft;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.commands.Command_tranxcraft;
import com.wickedgaminguk.tranxcraft.listeners.ListenerLoader;
import com.wickedgaminguk.tranxcraft.listeners.PlayerListener;
import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.modules.WarnModule;
import com.wickedgaminguk.tranxcraft.utils.BanUtils;
import com.wickedgaminguk.tranxcraft.utils.LogUtils;
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

    public TranxCraft plugin;
    public YamlConfig config;
    public BukkitCommandHandler handler;
    public MySQL database;
    public ListenerLoader listenerLoader;
    public ModuleLoader modLoader;
    public BanUtils banUtils;
    public LogUtils logUtils;
    public SqlModule sqlModule;
    public WarnModule warnModule;

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

        database = new MySQL(config.getString("mysql.hostname"), config.getString("mysql.port"), config.getString("mysql.username"), config.getString("mysql.password"), config.getString("mysql.database"));
        database.openConnection();

        handler.setCommandLocation(Command_tranxcraft.class.getPackage());

        listenerLoader = new ListenerLoader(plugin);
        listenerLoader.loadListeners(PlayerListener.class.getPackage());
        
        modLoader = new ModuleLoader(plugin);
        modLoader.loadModules(SqlModule.class.getPackage());
        modLoader.loadModules(new File(plugin.getDataFolder() + "/modules").listFiles());

        sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
        warnModule = (WarnModule) ModuleLoader.getModule("WarnModule");

        if (database.isOpen()) {
            if (!sqlModule.isInitialized()) {
                LoggerUtils.info(plugin, "Initializing database...");
                sqlModule.initialize();
                LoggerUtils.info(plugin, "Database initialized.");
            }
        }
        banUtils = new BanUtils(plugin);
        logUtils = new LogUtils(plugin, Level.parse(sqlModule.getConfigEntry("loglevel").toUpperCase()));
        
        if (banUtils.isBanned("90eb5d86-ed60-4165-a36e-bb77aa3c6664")) {
            LoggerUtils.info(plugin, "Bans Worked.");
        }

        if (banUtils.isExpired("90eb5d86-ed60-4165-a36e-bb77aa3c6664")) {
            LoggerUtils.info(plugin, "Ban has expired.");
        }
        
        if (banUtils.isExpired("test")) {
            LoggerUtils.info(plugin, "Ban has expired.");
        }
        else {
            LoggerUtils.info(plugin, "Test ban hasn't expired.");
        }
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
