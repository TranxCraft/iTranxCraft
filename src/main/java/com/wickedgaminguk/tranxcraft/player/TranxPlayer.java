package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TranxPlayer {

    private String uuid = "Not Set";
    private String name = "Not Set";
    private String latestIp = "Not Set";
    private String forumName = "Not Set";

    private Friend[] friends = null;

    private Rank rank = Rank.UNKNOWN;

    private int kills = 0;
    private int deaths = 0;
    private int votes = 0;
    private int playTime = 0;

    private double currency = 0;

    public TranxPlayer(String uuid, String player, String latestIp, String forumName, Rank rank, int kills, int deaths, int votes, double currency, int playTime) {
        this.uuid = uuid;
        this.name = player;
        this.latestIp = latestIp;
        this.forumName = forumName;
        this.rank = rank;
        this.kills = kills;
        this.deaths = deaths;
        this.votes = votes;
        this.playTime = playTime;
    }

    public TranxPlayer(String uuid) {
        this.uuid = uuid;
    }

    public TranxPlayer() {

    }

    public String getUuid() {
        return uuid;
    }

    public TranxPlayer setUuid(String uuid) {
        this.uuid = uuid;
        save();
        return this;
    }

    public String getName() {
        return name;
    }

    public TranxPlayer setName(String player) {
        this.name = player;
        save();
        return this;
    }

    public String getLatestIp() {
        return latestIp;
    }

    public TranxPlayer setLatestIp(String latestIp) {
        this.latestIp = latestIp;
        save();
        return this;
    }

    public String getForumName() {
        return forumName;
    }

    public TranxPlayer setForumName(String forumName) {
        this.forumName = forumName;
        save();
        return this;
    }

    public int getKills() {
        return kills;
    }

    public TranxPlayer setKills(int kills) {
        this.kills = kills;
        save();
        return this;
    }

    public int getDeaths() {
        return deaths;
    }

    public TranxPlayer setDeaths(int deaths) {
        this.deaths = deaths;
        save();
        return this;
    }

    public Rank getRank() {
        return rank;
    }

    public TranxPlayer setRank(Rank rank) {
        this.rank = rank;
        save();
        return this;
    }

    public int getVotes() {
        return votes;
    }

    public TranxPlayer setVotes(int votes) {
        this.votes = votes;
        save();
        return this;
    }

    public Friend[] getFriends() {
        return friends;
    }

    public TranxPlayer setFriends(Friend[] friends) {
        this.friends = friends;
        save();
        return this;
    }

    public double getCurrency() {
        return currency;
    }

    public TranxPlayer setCurrency(double currency) {
        this.currency = currency;
        save();
        return this;
    }

    public TranxPlayer addToCurrency(double currency) {
        this.currency += currency;
        save();
        return this;
    }

    public int getPlayTime() {
        return playTime;
    }

    public TranxPlayer setPlayTime(int playTime) {
        this.playTime = playTime;
        save();
        return this;
    }

    public TranxPlayer addToPlayTime(int playTime) {
        this.playTime += playTime;
        save();
        return this;
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

    /**
     * Uploads the data to the MySQL server.
     */
    public void save() {
        SqlModule sql = (SqlModule) ModuleLoader.getModule("SqlModule");
        sql.getDatabase().update("UPDATE `players` SET `player` = ?, `latestip` = ?, `rank` = ?, `kills` = ?, `deaths` = ?, `forumname` = ?, `votes` = ?, `currency` = ?, `playtime` = ? WHERE `uuid` = ?", getName(), getLatestIp(), getRank().toString().toLowerCase(), String.valueOf(getKills()), String.valueOf(getDeaths()), getForumName(), String.valueOf(getVotes()), String.valueOf(getCurrency()), String.valueOf(getPlayTime()), getUuid());
    }

    public static TranxPlayer loadFromSql(TranxCraft plugin, String uuid) {
        DebugUtils.debug(3, StrUtils.concatenate("SELECT * FROM `players` WHERE `uuid` = ", uuid));

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", uuid);
        TranxPlayer player = new TranxPlayer(uuid);

        try {
            if (result == null) {
                return null;
            }

            if (!result.isBeforeFirst()) {
                return null;
            }

            result.next();

            DebugUtils.debug(3, DebugUtils.resultSetToJSON(result));

            player.setName(result.getString("player"))
                    .setLatestIp(result.getString("latestip"))
                    .setKills(result.getInt("kills"))
                    .setDeaths(result.getInt("deaths"))
                    .setRank(Rank.valueOf(result.getString("rank").toUpperCase()))
                    .setForumName(result.getString("forumname"))
                    .setVotes(result.getInt("votes"))
                    .setFriends(getFriends(result.getString("friends")))
                    .setCurrency(result.getDouble("currency"))
                    .setPlayTime(result.getInt("playtime"));

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

            player.setName(result.getString("player"))
                    .setLatestIp(result.getString("latestip"))
                    .setKills(result.getInt("kills"))
                    .setDeaths(result.getInt("deaths"))
                    .setRank(Rank.valueOf(result.getString("rank").toUpperCase()))
                    .setForumName(result.getString("forumname"))
                    .setVotes(result.getInt("votes"))
                    .setFriends(getFriends(result.getString("friends")))
                    .setCurrency(result.getDouble("currency"))
                    .setPlayTime(result.getInt("playtime"));

            return player;
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
            return null;
        }
    }

    public static Friend[] getFriends(String result) {
        if (result != null && !result.isEmpty()) {
            String[] friendsString = StrUtils.removeWhitespace(result).split(",");

            List<Friend> friends = new ArrayList<>();

            for (String friend : friendsString) {
                if (ValidationUtils.isValidUuid(friend)) {
                    friends.add(new Friend(friend));
                }
            }

            return new Friend[friends.size()];
        }
        else {
            return new Friend[]{};
        }
    }
}
