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

    public TranxPlayer getPlayer(Player player) {
        if (playerCache.containsKey(player.getUniqueId().toString())) {
            return playerCache.get(player.getUniqueId().toString());
        }
        
        return TranxPlayer.loadFromSql(plugin, player.getUniqueId().toString());
    }
    
    public TranxPlayer getPlayer(CommandSender sender) {
        return getPlayer((Player) sender);
    }

    public TranxPlayer[] getPlayers() {
        if (playerCache.isEmpty()) {
            loadCache();
        }
        
        return (TranxPlayer[]) playerCache.entrySet().toArray();
    }

    public void insertPlayer(Player player) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `players` WHERE `uuid` = ?", player.getUniqueId().toString());

        DebugUtils.debug(2, DebugUtils.resultSetToJSON(result));

        TranxPlayer tPlayer = new TranxPlayer(
                player.getUniqueId().toString(),
                player.getName(),
                player.getAddress().getHostString(),
                "Not Set",
                Rank.PLAYER,
                0,
                0
        );

        try {
            playerCache.put(tPlayer.getUuid(), tPlayer);

            if (result.isBeforeFirst()) {
                DebugUtils.debug(1, StrUtils.concatenate("Data found for ", tPlayer.getName()));
                plugin.sqlModule.getDatabase().update("UPDATE `players` SET `player` = ?, `latestip` = ? WHERE `uuid` = ?;", tPlayer.getName(), tPlayer.getLatestIp(), tPlayer.getUuid());

                return;
            }

            plugin.sqlModule.getDatabase().update("INSERT INTO `players` (`uuid`, `player`, `latestip`, `kills`, `deaths`, `forumname`, `rank`) VALUES (?, ?, ?, ?, ?, ?, ?);", player.getUniqueId().toString(), player.getName(), player.getAddress().getHostString(), "0", "0", "Not Set", Rank.PLAYER.toString());
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
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
                        result.getInt("deaths")
                ));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex.getMessage());
        }
        
        playerCache.putAll(players);
        
        return playerCache;
    }
}
