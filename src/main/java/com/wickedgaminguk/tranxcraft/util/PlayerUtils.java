package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils extends Util {

    private static boolean staffMode;

    public static void reportPlayer(Player sender, Player player, String report) {
        broadcastMessage(StrUtils.concatenate(ChatColor.RED, "[REPORTS] ", ChatColor.GOLD, sender.getName(), " has reported ", player.getName(), " for ", report), Rank.MODERATOR);
        plugin.sqlModule.getDatabase().update("INSERT INTO `reports` (`sender`, `player`, `report`, `status`) VALUES (?, ?, ?, 'open');", sender.getName(), player.getName(), report);
        plugin.mailModule.sendEmail(Rank.MODERATOR, StrUtils.concatenate("[TranxCraft] ", player.getName(), " has been reported."), StrUtils.concatenate("Hi Admin,<br>", player.getName(), " has been reported by ", sender.getName(), " for ", report));
    }

    public static void broadcastMessage(String message, Rank rank) {
        for (TranxPlayer player : plugin.playerManager.getPlayers()) {
            if (player.hasRank(rank)) {
                player.getPlayer().sendMessage(message);
            }
        }
    }

    public static Player getPlayer(final String name) {
        return Bukkit.getPlayer(name);
    }

    public static boolean checkPermissions(CommandSender sender, Rank rank) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (plugin.playerManager.getPlayer(sender).hasRank(rank)) {
            return true;
        }

        return false;
    }

    public static boolean checkPermissions(CommandSender sender, String rankType) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (ValidationUtils.isInEnum(rankType.toUpperCase(), Rank.class)) {
            if (plugin.playerManager.getPlayer(sender).hasRank(Rank.valueOf(rankType.toUpperCase()))) {
                return true;
            }
        }
        else {
            DebugUtils.debug(1, StrUtils.concatenate("Invalid rank chosen ", rankType));
        }

        return false;
    }
}
