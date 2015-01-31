package com.wickedgaminguk.tranxcraft.modules;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlModule extends Module {

    private TranxCraft plugin;
    private MySQL database;

    public SqlModule(TranxCraft plugin) {
        this.plugin = plugin;
        this.database = plugin.database;
    }

    public boolean isInitialized() {
        return false;
    }

    public void initialize() {
        getDatabase().update("CREATE TABLE IF NOT EXISTS `bans` (`uuid` binary(36) DEFAULT NULL, `player` text, `admin` text, `reason` text, `ip` text, `expiry` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `config` (`config` text, `entry` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `admins` (`uuid` binary(36) DEFAULT NULL, `player` text, `ip` text, `rank` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `players` (`uuid` binary(36) DEFAULT NULL, `player` text, `latestip` text, `kills` int(11) DEFAULT NULL, `deaths` int(11) DEFAULT NULL, `forumname` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    }

    public MySQL getDatabase() {
        return this.database;
    }

    public boolean getBoolean(String entry) {
        String response = getConfigEntry(entry);

        return response.equals("1");
    }

    public String getConfigEntry(String entry) {
        ResultSet response = getDatabase().query("SELECT * FROM `config` WHERE `config` = ?", entry);

        try {
            response.next();
            return response.getString("entry");
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
            return "";
        }
    }

    public int getInt(String entry) {
        return Integer.valueOf(getConfigEntry(entry));
    }

    public double getDouble(String entry) {
        return Double.valueOf(getConfigEntry(entry));
    }
}
