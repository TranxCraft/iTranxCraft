package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.AdminManager;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import net.pushover.client.MessagePriority;
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
        plugin.pushoverModule.sendNotifications(Rank.MODERATOR, MessagePriority.NORMAL, StrUtils.concatenate(player.getName(), " has been reported."), StrUtils.concatenate(player.getName(), " has been reported by ", sender.getName(), " for ", report));
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

        if (rank.getRankLevel() >= Rank.MODERATOR.getRankLevel()) {
            if (AdminManager.isAdmin(((Player) sender).getUniqueId().toString())) {
                if (Admin.fromUuid(((Player) sender).getUniqueId().toString()).getRank().getRankLevel() >= rank.getRankLevel()) {
                    return true;
                }
            }
        }

        TranxPlayer player = plugin.playerManager.getPlayer(sender);

        DebugUtils.debug(2, player.getName() + " has a rank of " + player.getRank().toString());

        if (plugin.playerManager.getPlayer(sender).hasRank(rank)) {
            return true;
        }

        return false;
    }

    public static boolean checkPermissions(CommandSender sender, String rankType) {
        if (ValidationUtils.isInEnum(rankType, Rank.class)) {
            return checkPermissions(sender, Rank.valueOf(rankType));
        }
        else {
            DebugUtils.debug("Invalid Rank " + rankType);
            return false;
        }
    }
}
