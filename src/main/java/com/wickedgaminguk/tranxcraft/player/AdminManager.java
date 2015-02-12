package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AdminManager {

    private TranxCraft plugin;

    private static HashMap<String, Admin> adminCache = new HashMap<>();

    public AdminManager(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Admin> getAdminsFromIp(String ip) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `ip` = ?", ip);
        HashMap<String, Admin> admins = new HashMap<>();

        try {
            while (result.next()) {
                admins.put(result.getString("uuid"), loadAdmin(result.getString("uuid")));
            }
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex);
        }

        return admins;
    }

    public void addAdmin(String uuid, String player, String ip, Rank rank) {
        if (getAdminCache().containsKey(uuid)) {
            return;
        }
        else {
            plugin.sqlModule.getDatabase().update("INSERT IF NOT EXISTS INTO `admins` (`uuid`, `player`, `ip`, `rank`) VALUES (?, ?, ?, ?)", uuid, player, ip, rank.toString().toLowerCase());
            getAdminCache().put(uuid, new Admin().setUuid(uuid).setPlayerName(player).setIp(ip).setRank(rank));
        }
    }

    public void removeAdmin(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `admins` WHERE `uuid` = ?", uuid);
        getAdminCache().remove(uuid);
    }

    public void setAdmin(String uuid, Rank rank) {
        plugin.sqlModule.getDatabase().update("UPDATE `admins` SET `rank` = ? WHERE `uuid` = ?", uuid, rank.toString().toLowerCase());
        getAdminCache().put(uuid, loadAdmin(uuid).setRank(rank));
    }

    public int loadCache() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");
        int adminCount = 0;
        
        try {
            while (result.next()) {
                adminCount++;

                loadAdmin(result);
            }
        }
        catch(SQLException ex) {
            plugin.debugUtils.debug(ex);
        }
        
        return adminCount;
    }
    
    public Admin loadAdmin(ResultSet result) {
        Admin admin = new Admin();

        try {
            result.next();

            admin.setUuid(result.getString("uuid"));
            admin.setPlayerName(result.getString("player"));
            admin.setIp(result.getString("ip"));

            if(ValidationUtils.isInEnum(result.getString("rank").toUpperCase(), Rank.class)) {
                admin.setRank(Rank.valueOf(result.getString("rank").toUpperCase()));
            }
            
            admin.setEmail(result.getString("email"));

            getAdminCache().put(admin.getUuid(), admin);
        }
        catch (SQLException ex) {

        }
        
        return admin;
    }
    
    public Admin loadAdmin(String uuid) {
        return loadAdmin(uuid, false);
    }

    public Admin loadAdmin(String uuid, boolean overrideCache) {
        if (overrideCache == true) {
            getAdminCache().remove(uuid);
        }
        else if (getAdminCache().containsKey(uuid)) {
            return getAdminCache().get(uuid);
        }

        return loadAdmin(plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `uuid` = ?", uuid));
    }

    public Admin[] getAdmins() {
        return (Admin[]) getAdminCache().entrySet().toArray();
    }

    /** Gets the cache of Admin data.
     * @return The cache of Admin data.
     */
    public static HashMap<String, Admin> getAdminCache() {
        return adminCache;
    }

    public static void clearAdminCache() {
        adminCache.clear();
    }

    public static void reloadAdminCache() {
        clearAdminCache();
    }
    
    public static boolean isAdmin(String uuid) {
        return getAdminCache().containsKey(uuid);
    }
}
