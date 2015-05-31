package com.wickedgaminguk.tranxcraft.modules;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StatisticManager;
import com.wickedgaminguk.tranxcraft.util.StrUtils;

import java.sql.PreparedStatement;
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
    
    public String getEntry(String table, String entry, String value, String columnLabel) {
        DebugUtils.debug(3, StrUtils.concatenate("SELECT * FROM `", table, "` WHERE `", entry, "` = '", value, "';"));

        ResultSet result = getDatabase().query(StrUtils.concatenate("SELECT * FROM `", table, "` WHERE `", entry, "` = '" , value, "';"));

        try {
            result.next();
            return result.getString(columnLabel);
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
            return "";
        }
    }

    public String getConfigEntry(String entry) {
        return getEntry("config", "config", entry, "entry");
    }
    
    public String getStatistic(String statistic) {
        String result = getEntry("statistics", "statistic", statistic, "value");

        if (result.isEmpty()) {
            return "0";
        }
        else {
            return result;
        }
    }
    
    public int getRowCount(String table) {
        DebugUtils.debug(3, StrUtils.concatenate("SELECT COUNT(*) FROM `", table, "`;"));

        ResultSet result = getDatabase().query(StrUtils.concatenate("SELECT COUNT(*) FROM `", table, "`;"));

        try {
            if (!result.isBeforeFirst()) {
                return 0;
            }

            result.next();
            return result.getInt(1);
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex);
            return 0;
        }
    }

    public void execute(PreparedStatement statement) {
        try {
            statement.execute();
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex);
        }
    }

    public void incrementStatistic(String statistic) {
        incrementStatistic(statistic, 1);
    }

    public void incrementStatistic(String statistic, int amount) {
        DebugUtils.debug(3, StrUtils.concatenate("INSERT INTO `statistics` (`statistic`, `value`) VALUES('", statistic, "', ", amount, ") ON DUPLICATE KEY UPDATE value = value + ", amount));

        //getDatabase().update("INSERT INTO `statistics` (`statistic`, `value`) VALUES('" + statistic + "', " + amount + ") ON DUPLICATE KEY UPDATE value = value + " + amount);

        StatisticManager.addStatistic(getDatabase().prepare("INSERT INTO `statistics` (`statistic`, `value`) VALUES(?, ?) ON DUPLICATE KEY UPDATE value = value  + ?", statistic, String.valueOf(amount), String.valueOf(amount)));
    }
}
