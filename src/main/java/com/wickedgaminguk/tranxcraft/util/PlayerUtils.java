package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.AdminManager;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import net.pravian.pendulum.Pendulum;
import net.pushover.client.MessagePriority;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils extends Util {

    private static boolean staffMode;
    private static Pendulum timer = Pendulum.instance();

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
        return !(sender instanceof Player) || plugin.playerManager.getPlayer(sender).hasRank(rank);

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

    public static Pendulum getTimer() {
        return timer;
    }
}
