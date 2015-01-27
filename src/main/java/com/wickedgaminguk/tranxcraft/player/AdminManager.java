package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AdminManager {

    private TranxCraft plugin;
    private HashMap<String, AdminType> adminCache;

    public AdminManager(TranxCraft plugin) {
        this.plugin = plugin;
        adminCache = new HashMap<>();
    }

    public AdminType getRank(Player player) {
        return getRank(player.getUniqueId().toString());
    }

    public AdminType getRank(String uuid) {
        if (adminCache.containsKey(uuid)) {
            return adminCache.get(uuid);
        }

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `uuid` = ?", uuid);

        try {
            result.next();
            return AdminType.valueOf(result.getString("rank").toUpperCase());
        }
        catch (SQLException ex) {
            plugin.logUtils.debug(ex.getMessage());
            return AdminType.UNKNOWN;
        }
    }

    public HashMap<String, String> getAdminsFromIp(String ip) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `ip` = ?", ip);
        HashMap<String, String> admins = new HashMap<>();

        try {
            while (result.next()) {
                admins.put(result.getString("player"), result.getString("rank"));
            }
        }
        catch (SQLException ex) {
            plugin.logUtils.debug(ex.getMessage());
        }

        return admins;
    }

    public void reloadCache() {
        adminCache.clear();
        loadCache();
    }

    public void loadCache() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");

        try {
            while (result.next()) {
                adminCache.put(result.getString("player"), AdminType.valueOf(result.getString("rank").toUpperCase()));
            }
        }
        catch (SQLException ex) {
            plugin.logUtils.debug(ex.getMessage());
        }
    }

    public void addAdmin(String uuid, String player, String ip, AdminType rank) {
        plugin.sqlModule.getDatabase().update("INSERT INTO `admins` (`uuid`, `player`, `ip`, `rank`) VALUES (?, ?, ?, ?)", uuid, player, ip, rank.toString().toLowerCase());
        adminCache.put(uuid, rank);
    }

    public void removeAdmin(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `admins` WHERE `uuid` = ?", uuid);
        adminCache.remove(uuid);
    }

    public void promoteAdmin(String uuid, AdminType rank) {
        plugin.sqlModule.getDatabase().update("UPDATE `admins` SET `rank` = ? WHERE `uuid` = ?", uuid, rank.toString().toLowerCase());
        adminCache.put(uuid, rank);
    }

    public enum AdminType {
        COMMANDER, ADMIN, UNKNOWN
    }
}
