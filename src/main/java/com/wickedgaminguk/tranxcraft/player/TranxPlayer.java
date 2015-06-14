package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TranxPlayer {

    private String uuid = "Not Set";
    private String name = "Not Set";
    private String latestIp = "Not Set";
    private String forumName = "Not Set";

    private Rank rank = Rank.UNKNOWN;

    private int kills = 0;
    private int deaths = 0;

    public TranxPlayer(String uuid, String player, String latestIp, String forumName, Rank rank, int kills, int deaths) {
        this.uuid = uuid;
        this.name = player;
        this.latestIp = latestIp;
        this.forumName = forumName;
        this.rank = rank;
        this.kills = kills;
        this.deaths = deaths;
    }

    public TranxPlayer(String uuid) {
        this.uuid = uuid;
    }

    public TranxPlayer() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        save();
    }

    public String getName() {
        return name;
    }

    public void setName(String player) {
        this.name = player;
        save();
    }

    public String getLatestIp() {
        return latestIp;
    }

    public void setLatestIp(String latestIp) {
        this.latestIp = latestIp;
        save();
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
        save();
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
        save();
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        save();
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        save();
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getUuid()));
    }
    
    public boolean hasRank(Rank rank) {
        return this.rank.getRankLevel() >= rank.getRankLevel();
    }
    
    public Admin getAdmin() {
        if (isAdmin()) {
            return Admin.fromUuid(uuid);
        }
        else {
            return new Admin();
        }
    }
    
    public boolean isAdmin() {
        Admin admin = Admin.fromUuid(uuid);
        
        return admin.getRank() != null;
    }

    /** Uploads the data to the MySQL server.
     *
     */
    public void save() {
        SqlModule sql = (SqlModule) ModuleLoader.getModule("SqlModule");
        sql.getDatabase().update("UPDATE `players` SET `player` = ?, `latestip` = ?, `rank` = ?, `kills` = ?, `deaths` = ?, `forumname` = ? WHERE `uuid` = ?", getName(), getLatestIp(), getRank().toString().toLowerCase(), String.valueOf(getKills()), String.valueOf(getDeaths()), getForumName(), getUuid());
    }

    public static TranxPlayer loadFromSql(TranxCraft plugin, String uuid) {
        DebugUtils.debug(3, StrUtils.concatenate("SELECT * FROM `players` WHERE `uuid` = ", uuid));

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", uuid);
        TranxPlayer player = new TranxPlayer(uuid);

        try {
            if (!result.isBeforeFirst()) {
                return null;
            }

            result.next();

            DebugUtils.debug(3, DebugUtils.resultSetToJSON(result));

            player.setName(result.getString("player"));
            player.setLatestIp(result.getString("latestip"));
            player.setKills(result.getInt("kills"));
            player.setDeaths(result.getInt("deaths"));
            player.setRank(Rank.valueOf(result.getString("rank").toUpperCase()));
            player.setForumName(result.getString("forumname"));

            return player;
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
            return null;
        }
    }

    public static TranxPlayer loadFromSql(TranxCraft plugin, InetAddress ip) {
        DebugUtils.debug(3, StrUtils.concatenate("SELECT * FROM `players` WHERE `latestip` = ", ip.getHostAddress()));

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `latestip` = ?", ip.getHostAddress());
        TranxPlayer player = new TranxPlayer();

        try {
            if (result == null) {
                return null;
            }

            if (!result.isBeforeFirst()) {
                return null;
            }

            DebugUtils.debug(3, DebugUtils.resultSetToJSON(result));

            result.next();

            DebugUtils.debug(result.getString("rank"));

            player.setName(result.getString("player"));
            player.setLatestIp(result.getString("latestip"));
            player.setKills(result.getInt("kills"));
            player.setDeaths(result.getInt("deaths"));
            player.setRank(Rank.valueOf(result.getString("rank").toUpperCase()));
            player.setForumName(result.getString("forumname"));

            return player;
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
            return null;
        }
    }
}
