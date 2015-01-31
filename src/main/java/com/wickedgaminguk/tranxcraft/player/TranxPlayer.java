package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranxPlayer {

    private String uuid;
    private String player;
    private String latestIp;
    private String forumName;

    private int kills;
    private int deaths;

    public TranxPlayer(String uuid, String player, String latestIp, String forumName, int kills, int deaths) {
        this.uuid = uuid;
        this.player = player;
        this.latestIp = latestIp;
        this.forumName = forumName;
        this.kills = kills;
        this.deaths = deaths;
    }

    public TranxPlayer(String uuid) {
        this.uuid = uuid;
    }

    public TranxPlayer() {

    }

    public static TranxPlayer loadFromSql(TranxCraft plugin, String uuid) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", uuid);
        TranxPlayer player = new TranxPlayer(uuid);

        try {
            result.next();

            player.setPlayer(result.getString("player"));
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
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
}
