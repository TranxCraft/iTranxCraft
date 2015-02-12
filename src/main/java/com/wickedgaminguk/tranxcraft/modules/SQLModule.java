package com.wickedgaminguk.tranxcraft.modules;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlModule extends Module<TranxCraft> {

    public boolean isInitialized() {
        return false;
    }

    public void initialize() {
        getDatabase().update("CREATE TABLE IF NOT EXISTS `bans` (`uuid` binary(36) DEFAULT NULL, `player` text, `admin` text, `reason` text, `ip` text, `expiry` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `config` (`config` text, `entry` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `admins` (`uuid` binary(36) DEFAULT NULL, `player` text, `ip` text, `rank` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `players` (`uuid` binary(36) DEFAULT NULL, `player` text, `latestip` text, `kills` int(11) DEFAULT NULL, `deaths` int(11) DEFAULT NULL, `forumname` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `announcements` (`announcement` text, `interval` int(11) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
        getDatabase().update("CREATE TABLE IF NOT EXISTS `statistics` (`statistic` text, `value` text) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    }

    public MySQL getDatabase() {
        return plugin.database;
    }
    
    public String getEntry(String table, String entry, String value) {
        ResultSet result = getDatabase().query(StrUtils.concatenate("SELECT * FROM `", table, "` WHERE `", entry, "` = '" , value, "';"));

        try {
            result.next();
            return result.getString(value);
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
            return "";
        }
    }

    public String getConfigEntry(String entry) {
        return getEntry("config", "config", entry);
    }
    
    public String getStatistic(String statistic) {
        return getEntry("statistics", "statistic", statistic);
    }
    
    public int getRowCount(String table) {
        ResultSet result = getDatabase().query("SELECT COUNT(*) FROM ?;", table);
        
        try {
            result.next();
            return result.getInt("rowcount");
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex);
            return -1;
        }
    }

    public void setStatistic(String statistic, String value) {
        getDatabase().update("INSERT INTO `statistics` (`statistic`, `value`) VALUES (?, ?);", statistic, value);
    }
}
