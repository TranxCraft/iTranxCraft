package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PlayerManager {

    private TranxCraft plugin;
    private HashMap<String, TranxPlayer> playerCache = new HashMap<>();

    public PlayerManager(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public TranxPlayer getPlayer(String uuid) {
        if (playerCache.containsKey(uuid)) {
            return playerCache.get(uuid);
        }
        
        return TranxPlayer.loadFromSql(plugin, uuid);
    }
    
    public TranxPlayer getPlayer(CommandSender sender) {
        return getPlayer((Player) sender);
    }

    public TranxPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId().toString());
    }

    public TranxPlayer[] getPlayers() {
        if (playerCache.isEmpty()) {
            loadCache();
        }

        return (playerCache.values().toArray(new TranxPlayer[playerCache.values().size()]));
    }

    public void insertPlayer(Player player) {
        if (!playerCache.containsKey(player.getUniqueId().toString())) {
            ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", player.getUniqueId().toString());

            DebugUtils.debug(2, DebugUtils.resultSetToJSON(result));

            TranxPlayer tPlayer = new TranxPlayer(
                    player.getUniqueId().toString(),
                    player.getName(),
                    player.getAddress().getHostString(),
                    "Not Set",
                    Rank.PLAYER,
                    0,
                    0,
                    0,
                    0,
                    0
            );

            try {
                if (result.isBeforeFirst()) {
                    DebugUtils.debug(1, StrUtils.concatenate("Data found for ", tPlayer.getName()));

                    result.next();

                    playerCache.put(player.getUniqueId().toString(), new TranxPlayer(
                            result.getString("uuid"),
                            result.getString("player"),
                            result.getString("latestip"),
                            result.getString("forumname"),
                            Rank.valueOf(result.getString("rank").toUpperCase()),
                            result.getInt("kills"),
                            result.getInt("deaths"),
                            result.getInt("votes"),
                            result.getDouble("currency"),
                            result.getInt("playtime")
                    ));

                    plugin.sqlModule.getDatabase().update("UPDATE `players` SET `player` = ?, `latestip` = ? WHERE `uuid` = ?;", tPlayer.getName(), tPlayer.getLatestIp(), tPlayer.getUuid());
                }
                else {
                    playerCache.put(tPlayer.getUuid(), tPlayer);
                    plugin.sqlModule.getDatabase().update("INSERT INTO `players` (`uuid`, `player`, `latestip`, `kills`, `deaths`, `forumname`, `rank`, `currency`, `playtime`) VALUES (?, ?, ?, ?, ?, ?, ?);", player.getUniqueId().toString(), player.getName(), player.getAddress().getHostString(), "0", "0", "Not Set", Rank.PLAYER.toString(), "0", "0");
                }
            }
            catch (SQLException ex) {
                DebugUtils.debug(ex);
            }
        }
    }

    public HashMap<String, TranxPlayer> loadCache() {
        playerCache.clear();
        
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players`");
        HashMap<String, TranxPlayer> players = new HashMap<>();

        try {
            while (result.next()) {
                players.put(result.getString("uuid"), new TranxPlayer(
                        result.getString("uuid"),
                        result.getString("player"),
                        result.getString("latestip"),
                        result.getString("forumname"),
                        Rank.valueOf(result.getString("rank").toUpperCase()),
                        result.getInt("kills"),
                        result.getInt("deaths"),
                        result.getInt("votes"),
                        result.getDouble("currency"),
                        result.getInt("playtime")
                ));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex.getMessage());
        }
        
        playerCache.putAll(players);
        
        return playerCache;
    }

    public void reloadCache() {
        playerCache.clear();
        loadCache();
    }
}
