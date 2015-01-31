package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.utils.ValidationUtils;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AdminManager {

    private TranxCraft plugin;

    public AdminManager(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public AdminType getRank(Player player) {
        return getRank(player.getUniqueId().toString());
    }

    public AdminType getRank(String uuid) {
        return loadAdmin(uuid).getRank();
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
            plugin.debugUtils.debug(ex.getMessage());
        }

        return admins;
    }

    public void addAdmin(String uuid, String player, String ip, AdminType rank) {
        plugin.sqlModule.getDatabase().update("INSERT INTO `admins` (`uuid`, `player`, `ip`, `rank`) VALUES (?, ?, ?, ?)", uuid, player, ip, rank.toString().toLowerCase());
        Admin.getAdminCache().put(uuid, new Admin().setUuid(uuid).setPlayerName(player).setIp(ip).setRank(rank));
    }

    public void removeAdmin(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `admins` WHERE `uuid` = ?", uuid);
        Admin.getAdminCache().remove(uuid);
    }

    public void promoteAdmin(String uuid, AdminType rank) {
        plugin.sqlModule.getDatabase().update("UPDATE `admins` SET `rank` = ? WHERE `uuid` = ?", uuid, rank.toString().toLowerCase());
        Admin.getAdminCache().put(uuid, loadAdmin(uuid).setRank(rank));
    }

    public int loadCache() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");
        int adminCount = 0;
        
        try {
            while (result.next()) {
                Admin admin = new Admin();
                adminCount++;

                admin.setUuid(result.getString("uuid"));
                admin.setPlayerName(result.getString("player"));
                admin.setIp(result.getString("ip"));
                
                if(ValidationUtils.isInEnum(result.getString("rank").toUpperCase(), AdminType.class)) {
                    admin.setRank(AdminType.valueOf(result.getString("rank").toUpperCase()));
                }
                
                admin.setEmail(result.getString("email"));

                Admin.getAdminCache().put(admin.getUuid(), admin);
            }
        }
        catch(SQLException ex) {
            
        }
        
        return adminCount;
    }
    
    public Admin loadAdmin(String uuid) {
        return loadAdmin(uuid, false);
    }

    public Admin loadAdmin(String uuid, boolean overrideCache) {
        if (overrideCache == true) {
            Admin.getAdminCache().remove(uuid);
        }
        
        if (Admin.getAdminCache().containsKey(uuid)) {
            return Admin.getAdminCache().get(uuid);
        }

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins` WHERE `uuid` = ?", uuid);
        Admin admin = new Admin();

        try {
            result.next();

            admin.setUuid(uuid);
            admin.setPlayerName(result.getString("player"));
            admin.setIp(result.getString("ip"));
            admin.setRank(AdminType.valueOf(result.getString("rank").toUpperCase()));
            admin.setEmail(result.getString("email"));

            Admin.getAdminCache().put(uuid, admin);
        }
        catch (SQLException ex) {

        }

        return admin;
    }

    public Admin[] getAdmins() {
        return (Admin[]) Admin.getAdminCache().entrySet().toArray();
    }

    public enum AdminType {
        UNKNOWN(0), PLAYER(1), MODERATOR(2), ADMIN(3), LEADADMIN(4), EXECUTIVE(5), COMMANDER(6);

        private int rank;
        
        AdminType(int rank) {
            this.rank = rank;            
        }
        
        public int getRankLevel() {
            return this.rank;
        }
    }
}
