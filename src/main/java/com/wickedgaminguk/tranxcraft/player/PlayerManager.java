package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PlayerManager {

    private TranxCraft plugin;
    private  HashMap<String, TranxPlayer> playerCache = new HashMap<>();

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
    
    public HashMap<String, TranxPlayer> loadCache() {
        playerCache.clear();
        
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");
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
            plugin.debugUtils.debug(ex.getMessage());
        }
        
        playerCache.putAll(players);
        
        return playerCache;
    }
}
