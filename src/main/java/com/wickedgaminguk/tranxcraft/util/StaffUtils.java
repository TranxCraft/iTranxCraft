package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffUtils extends Util {

    private static boolean staffMode;

    public static boolean getStaffMode() {
        return staffMode;
    }

    public static void setStaffMode(String sender, boolean mode) {
        staffMode = mode;

        if (mode == true) {
            Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender, " - Closing the server to all non staff players."));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!plugin.playerManager.getPlayer(player).hasRank(Rank.MODERATOR)) {
                    player.kickPlayer(StrUtils.concatenate(ChatColor.RED, "Sorry ", ChatColor.GOLD, player.getName(), ChatColor.RED, " Staff Mode has been enabled. Please come back later."));
                }
            }
        }
        else {
            Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.AQUA, sender, " - Opening the server to all players."));
        }
    }
}
