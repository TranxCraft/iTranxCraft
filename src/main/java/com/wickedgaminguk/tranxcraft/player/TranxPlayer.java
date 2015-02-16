package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TranxPlayer {

    private String uuid;
    private String name;
    private String latestIp;
    private String forumName;

    private Rank rank;

    private int kills;
    private int deaths;

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
    }

    public String getName() {
        return name;
    }

    public void setName(String player) {
        this.name = player;
    }

    public String getLatestIp() {
        return latestIp;
    }

    public void setLatestIp(String latestIp) {
        this.latestIp = latestIp;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getUuid()));
    }
    
    public boolean hasRank(Rank rank) {
        return this.rank.getRankLevel() > rank.getRankLevel();
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

    public static TranxPlayer loadFromSql(TranxCraft plugin, String uuid) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", uuid);
        TranxPlayer player = new TranxPlayer(uuid);

        try {
            result.next();

            player.setName(result.getString("player"));
            player.setLatestIp(result.getString("latestip"));
            player.setKills(result.getInt("kills"));
            player.setDeaths(result.getInt("deaths"));
            player.setForumName(result.getString("forumname"));

            return player;
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
            return player;
        }
    }

    public static TranxPlayer loadFromSql(TranxCraft plugin, InetAddress ip) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `ip` = ?", ip.getHostAddress());
        TranxPlayer player = new TranxPlayer();

        try {
            result.next();

            player.setName(result.getString("player"));
            player.setLatestIp(result.getString("latestip"));
            player.setKills(result.getInt("kills"));
            player.setDeaths(result.getInt("deaths"));
            player.setForumName(result.getString("forumname"));

            return player;
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
            return null;
        }
    }
}
