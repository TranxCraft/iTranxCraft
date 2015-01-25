package com.wickedgaminguk.tranxcraft.utils;

import com.wickedgaminguk.tranxcraft.Ban;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

public class BanUtils {
    
    private TranxCraft plugin;
    
    public BanUtils(TranxCraft plugin) {
        this.plugin = plugin;
    }
    
    public ResultSet selectBan(String uuid) {
        return plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `uuid` = ?", uuid);         
    }
    
    public Ban getBan(String uuid) {
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
    
    public boolean isBanned(Player player) {
        return isBanned(player.getUniqueId().toString());
    }
    
    public boolean isBanned(String uuid) {
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
        ResultSet response = plugin.sqlModule.getDatabase().query("SELECT * FROM `bans` WHERE `uuid` = ?", uuid);

        try {
            response.next();
            
            if (response.getString("expiry").isEmpty()) {
                return false;
            }

            Date currentDate = Date.from(Instant.ofEpochSecond(TimeUtils.getUnix()));
            Date expiryDate = Date.from(Instant.ofEpochSecond(Long.valueOf(response.getString("expiry"))));

            return expiryDate.before(currentDate);
        }
        catch (SQLException ex) {
            return false;
        }
    }
    
    public void addBan(Ban ban) {
        plugin.sqlModule.getDatabase().update("INSERT INTO `bans` (`uuid`, `player`, `admin`, `reason`, `ip`, `expiry`) VALUES (?,?,?,?,?,?)", ban.getUuid(), ban.getPlayer(), ban.getAdmin(), ban.getReason(), ban.getIp(), ban.getExpiry().toString());
    }
    
    public void removeBan(String uuid) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `bans` WHERE `uuid` = ?", uuid);        
    }
}
