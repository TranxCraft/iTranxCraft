package com.wickedgaminguk.tranxcraft.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.FetcherUtils;
import com.wickedgaminguk.tranxcraft.util.MinecraftUtils;
import com.wickedgaminguk.tranxcraft.util.StaffUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;
import java.util.UUID;

public class ServerListener extends Listener<TranxCraft> {
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPing(ServerListPingEvent event) {
        TranxPlayer player = TranxPlayer.loadFromSql(plugin, event.getAddress());
        
        if (player != null) {
            if (plugin.banManager.isBanned(player.getUuid())) {
                event.setMotd(StrUtils.concatenate(ChatColor.RED, "Sorry ", player.getName(), ", but you are", ChatColor.BOLD, " banned."));
            }
            else if (StaffUtils.getStaffMode() && !player.hasRank(Rank.MODERATOR)) {
                event.setMotd(ChatColor.RED + "Hey " + player.getName() + ", sadly, adminmode is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
            }
            else if (Bukkit.hasWhitelist() && !Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(player.getName()))) {
                event.setMotd(StrUtils.concatenate(ChatColor.RED, "Sorry ", player.getName(), ", but the whitelist is on - come back soon!", ChatColor.LIGHT_PURPLE, " <3"));
            }
            else if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() && !(player.isAdmin())) {
                event.setMotd(StrUtils.concatenate(ChatColor.RED, "Sorry ", player.getName(), ", but the server is full - come back soon!", ChatColor.LIGHT_PURPLE, " <3"));
            }
            else {
                event.setMotd(StrUtils.concatenate(ChatColor.GOLD, "Welcome back to the TranxCraft Private Alpha, ", ChatColor.GREEN, player.getName(), ChatColor.GOLD, "!", ChatColor.DARK_PURPLE, " Craftbukkit ", MinecraftUtils.getMinecraftVersion()));
            }
        }
        else if (plugin.banManager.isBanned(event.getAddress())) {
            event.setMotd(StrUtils.concatenate(ChatColor.RED, "You are banned."));
        }
        else if (StaffUtils.getStaffMode()) {
            event.setMotd(ChatColor.RED + "Adminmode enabled.");
        }
        else if (Bukkit.hasWhitelist()) {
            event.setMotd(StrUtils.concatenate(ChatColor.RED, "The whitelist is enabled."));
        }
        else if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            event.setMotd(StrUtils.concatenate(ChatColor.RED, "The Server is full."));
        }
        else {
            DebugUtils.debug(1, StrUtils.concatenate("No information found for IP address ", event.getAddress().getHostAddress()));
            event.setMotd(StrUtils.concatenate(ChatColor.GREEN, "TranxCraft Private Alpha", ChatColor.WHITE, " - ", ChatColor.DARK_PURPLE, "Craftbukkit ", MinecraftUtils.getMinecraftVersion()));
        }
    }

    /*@EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();

        String playerName = vote.getUsername();
        UUID playerId = FetcherUtils.fetchUuid(playerName);
        TranxPlayer player = plugin.playerManager.getPlayer(playerId.toString());

        player.setVotes(player.getVotes() + 1);

        Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.GOLD, player.getName(), ChatColor.GREEN, " has voted for TranxCraft on ", vote.getServiceName(), "! They have been rewarded with $500!"));

        //Needs eco system
    }*/
}
