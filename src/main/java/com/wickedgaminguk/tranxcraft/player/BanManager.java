package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Ban;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.entity.Player;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class BanManager {

    private TranxCraft plugin;
    private HashMap<String, Ban> banCache;

    /** Creates a new instance of BanUtils, where you can run various different functions related to bans.
     * @param plugin The main plugin instance, used to reference the SqlModule.
     */
    public BanManager(TranxCraft plugin) {
        this.plugin = plugin;
        banCache = new HashMap<>();
    }

    /**
     * @param uuid The UUID of the player that you want to get.
     * @return A ban instance of the passed UUID.
     */
    public Ban getBan(String uuid) {
        if (banCache.containsKey(uuid)) {
            return banCache.get(uuid);
        }

        ResultSet response = selectBan(uuid);
        Ban ban = new Ban();

        try {
            response.next();

            ban.setUuid(response.getString("uuid"));
            ban.setPlayer(response.getString("player"));
            ban.setAdmin(response.getString("admin"));
            ban.setReason(response.getString("reason"));
            ban.setIp(response.getString("ip"));
            ban.setExpiry(response.getString("expiry"));
        }
        catch (SQLException ex) {

        }

        return ban;
    }

    /** Selects a ban from the database.
     * @param uuid The UUID of the ban that you want to retrieve.
     * @return A ResultSet of the SQL query.
     */
    private ResultSet selectBan(String uuid) {
        return plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `uuid` = ?", uuid);
    }
    
    private ResultSet selectBan(InetAddress ip) {
        return plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `ip` = ?", ip.getHostAddress());
    }

    /** Checks if a player is banned.
     * @param player The player that you want to check.
     * @return A boolean on if the player is banned or not.
     */
    public boolean isBanned(Player player) {
        return isBanned(player.getUniqueId().toString());
    }

    /** Checks if an UUID is banned.
     * @param uuid The UUID that you want to check.
     * @return A boolean on if the player is banned or not.
     */
    public boolean isBanned(String uuid) {
        if (banCache.containsKey(uuid)) {
            return true;
        }

        ResultSet ban = selectBan(uuid);

        try {
            ban.next();
            return !ban.getString("uuid").isEmpty();
        }
        catch (SQLException ex) {
            return false;
        }
    }
    
    public boolean isBanned(InetAddress ip) {
        ResultSet ban = selectBan(ip);
        
        try {
            ban.next();
            return !ban.getString("uuid").isEmpty();
        }
        catch (SQLException ex) {
            return false;
        }
    }

    /** Checks if an UUID ban has expired.
     * @param uuid The UUID that you want to check.
     * @return A boolean on if the ban has expired or not.
     */
    public boolean isExpired(String uuid) {
        Date currentDate = Date.from(Instant.ofEpochSecond(TimeUtils.getUnix()));

        if (banCache.containsKey(uuid)) {
            Date expiryDate = Date.from(Instant.ofEpochSecond(Long.valueOf(banCache.get(uuid).getExpiry())));

            return expiryDate.before(currentDate);
        }

        ResultSet response = plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `uuid` = ?", uuid);

        try {
            response.next();

            if (response.getString("expiry").isEmpty()) {
                return false;
            }

            Date expiryDate = Date.from(Instant.ofEpochSecond(Long.valueOf(response.getString("expiry"))));

            return expiryDate.before(currentDate);
        }
        catch (SQLException ex) {
            return false;
        }
    }

    /** Adds a new ban to the database.
     * @param ban A ban instance that you want to add to the database.
     */
    public void addBan(Ban ban) {
        plugin.sqlModule.getDatabase().update("INSERT INTO `bans` (`uuid`, `player`, `admin`, `reason`, `ip`, `expiry`) VALUES (?,?,?,?,?,?)", ban.getUuid(), ban.getPlayer(), ban.getAdmin(), ban.getReason(), ban.getIp(), ban.getExpiry().toString());
        banCache.put(ban.getUuid(), ban);
    }

    /** Removes a ban from the database.
     * @param uuid The UUID of the ban that you want to remove.
     */
    public void removeBan(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `bans` WHERE `uuid` = ?", uuid);
        banCache.remove(uuid);
    }

    /** Removes a ban from the database.
     * @param ban A ban instance that you want to remove.
     */
    public void removeBan(Ban ban) {
        removeBan(ban.getUuid());   
    }

    /** Gets all of the bans and loads them into RAM for quicker access.
     * @return The amount of loaded bans.
     */
    public int loadCache() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `bans`");
        int banCount = 0;
        
        try {
            while (result.next()) {
                Ban ban = new Ban();
                banCount++;

                ban.setUuid(result.getString("uuid"));
                ban.setPlayer(result.getString("player"));
                ban.setAdmin(result.getString("admin"));
                ban.setReason(result.getString("reason"));
                ban.setIp(result.getString("ip"));
                ban.setExpiry(result.getString("expiry"));

                banCache.put(ban.getUuid(), ban);
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex.getMessage());
        }
        
        return banCount;
    }

    public void reloadCache() {
        banCache.clear();
        loadCache();
    }
}
