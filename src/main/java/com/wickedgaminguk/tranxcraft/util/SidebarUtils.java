package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Friend;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class SidebarUtils extends Util {

    private static ScoreboardManager manager = Bukkit.getScoreboardManager();

    public static Scoreboard createSidebar(String displayName, String content) {
        Scoreboard board = manager.getNewScoreboard();

        Objective bar = board.registerNewObjective("sidebar", "dummy");

        bar.setDisplaySlot(DisplaySlot.SIDEBAR);
        bar.setDisplayName(displayName);

        return board;
    }

    public static Objective addFriends(Objective bar, Friend[] friends) {
        for (Friend friend : friends) {
            bar.getScore(ChatColor.GREEN + friend.getName());
        }

        return bar;
    }

    public static void addToPlayer(Player player, Scoreboard board) {
        player.setScoreboard(board);
    }
}
