package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminManager {

    private TranxCraft plugin;

    private static HashMap<String, Admin> adminCache = new HashMap<>();
    private static List<Player> toggledAdminChat = new ArrayList<>();

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
            DebugUtils.debug(ex);
        }

        return admins;
    }

    public void addAdmin(String uuid, String player, String ip, Rank rank) {
        if (!getAdminCache().containsKey(uuid)) {
            plugin.sqlModule.getDatabase().update("INSERT INTO `admins` (`uuid`, `player`, `ip`, `rank`) VALUES (?, ?, ?, ?)", uuid, player, ip, rank.toString().toLowerCase());
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
            DebugUtils.debug(ex);
        }
        
        return adminCount;
    }
    
    public Admin loadAdmin(ResultSet result) {
        Admin admin = new Admin();

        try {
            admin.setUuid(result.getString("uuid"));
            admin.setPlayerName(result.getString("player"));
            admin.setIp(result.getString("ip"));

            if(ValidationUtils.isInEnum(result.getString("rank").toUpperCase(), Rank.class)) {
                admin.setRank(Rank.valueOf(result.getString("rank").toUpperCase()));
            }
            
            admin.setEmail(result.getString("email"));

            getAdminCache().put(admin.getUuid(), admin);
            admin.setInitalised(true);
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
        }
        
        return admin;
    }
    
    public Admin loadAdmin(String uuid) {
        return loadAdmin(uuid, false);
    }

    public Admin loadAdmin(String uuid, boolean overrideCache) {
        if (overrideCache) {
            getAdminCache().remove(uuid);
        }
        else if (getAdminCache().containsKey(uuid)) {
            return getAdminCache().get(uuid);
        }

        return loadAdmin(plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `uuid` = ?", uuid));
    }

    public Admin[] getAdmins() {
        if (getAdminCache().isEmpty()) {
            return new Admin[]{};
        }

        return (getAdminCache().values().toArray(new Admin[getAdminCache().values().size()]));
    }

    public List<Player> getToggledAdminChat() {
        return toggledAdminChat;
    }

    /** Gets the cache of Admin data.
     * @return The cache of Admin data.
     */
    public static HashMap<String, Admin> getAdminCache() {
        return adminCache;
    }

    public void reloadCache() {
        adminCache.clear();
        loadCache();
    }

    public static boolean isAdmin(String uuid) {
        return getAdminCache().containsKey(uuid);
    }
}
