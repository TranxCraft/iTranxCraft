package com.wickedgaminguk.tranxcraft.utils;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Ban;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class BanUtils {

    private TranxCraft plugin;
    private HashMap<String, Ban> banCache;

    public BanUtils(TranxCraft plugin) {
        this.plugin = plugin;
        banCache = new HashMap<>();
    }

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

    private ResultSet selectBan(String uuid) {
        return plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `uuid` = ?", uuid);
    }

    public boolean isBanned(Player player) {
        return isBanned(player.getUniqueId().toString());
    }

    public boolean isBanned(String uuid) {
        if (banCache.containsKey(uuid)) {
            return true;
        }

        ResultSet ban = selectBan(uuid);

        try {
            ban.next();
            return !ban.getString("UUID").isEmpty();
        }
        catch (SQLException ex) {
            return false;
        }
    }

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

    public void addBan(Ban ban) {
        plugin.sqlModule.getDatabase().update("INSERT INTO `bans` (`uuid`, `player`, `admin`, `reason`, `ip`, `expiry`) VALUES (?,?,?,?,?,?)", ban.getUuid(), ban.getPlayer(), ban.getAdmin(), ban.getReason(), ban.getIp(), ban.getExpiry().toString());
        banCache.put(ban.getUuid(), ban);
    }

    public void removeBan(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `bans` WHERE `uuid` = ?", uuid);
        banCache.remove(uuid);
    }

    public void loadCache() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");

        try {
            while (result.next()) {
                Ban ban = new Ban();

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
            plugin.logUtils.debug(ex.getMessage());
        }
    }
}
