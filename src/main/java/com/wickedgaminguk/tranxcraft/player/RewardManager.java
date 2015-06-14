package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import org.bukkit.Material;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class RewardManager {

    public RewardManager(TranxCraft plugin) {
        TranxCraft plugin1 = plugin;

        ResultSet mobResult = plugin.sqlModule.getDatabase().query("SELECT * FROM `reward_players` WHERE `type` = 'mob'");

        try {
            while (mobResult.next()) {
                HashMap<String, Double> mobValues = new HashMap<>();
                mobValues.put(mobResult.getString("item"), mobResult.getDouble("value"));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
        }

        ResultSet itemResult = plugin.sqlModule.getDatabase().query("SELECT * FROM `reward_players` WHERE `type` = 'item'");

        try {
            while (itemResult.next()) {
                HashMap<Material, Double> itemValues = new HashMap<>();
                itemValues.put(Material.valueOf(itemResult.getString("item").toUpperCase()), itemResult.getDouble("value"));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
        }
    }
}
