package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private TranxCraft plugin;

    public PlayerManager(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public TranxPlayer getPlayer(Player player) {
        return TranxPlayer.loadFromSql(plugin, player.getUniqueId().toString());
    }

    public TranxPlayer[] getPlayers() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `admins`");
        List<TranxPlayer> players = new ArrayList<>();

        try {
            while (result.next()) {
                players.add(new TranxPlayer(
                        result.getString("uuid"),
                        result.getString("player"),
                        result.getString("latestip"),
                        result.getString("forumname"),
                        result.getInt("kills"),
                        result.getInt("deaths")
                ));
            }
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex.getMessage());
        }

        return (TranxPlayer[]) players.toArray();
    }
}
