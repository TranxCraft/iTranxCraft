package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.RewardPlayerData;
import com.wickedgaminguk.tranxcraft.util.BanUtils;
import com.wickedgaminguk.tranxcraft.util.StaffUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener extends Listener<TranxCraft> {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        LoggerUtils.info(plugin, event.getHostname());

        if (!event.getHostname().contains("tranxcraft.com")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, StrUtils.concatenate(ChatColor.RED, "Please join on the following address\n", ChatColor.GOLD, "play.tranxcraft.com"));
        }

        if (BanUtils.getHardBans().containsKey(player.getUniqueId().toString())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(BanUtils.getHardBans().get(player.getUniqueId().toString())));
        }

        if (plugin.banManager.isBanned(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(plugin.banManager.getBan(player.getUniqueId().toString()).buildBanReason()));
        }

        if(StaffUtils.getStaffMode()) {
            if (!plugin.playerManager.getPlayer(player).hasRank(Rank.MODERATOR)) {
                player.sendMessage(StrUtils.concatenate(ChatColor.RED, "Sorry ", player.getName(), " Staff Mode is enabled. Come back soon!"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.sqlModule.incrementStatistic("global_player_joins");
        int playerCount = Integer.valueOf(plugin.sqlModule.getStatistic("global_player_joins"));
        plugin.playerManager.insertPlayer(event.getPlayer());

        Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.BLUE, "[Player Counter] ", playerCount, " players & ", plugin.sqlModule.getRowCount("players"), " unique players have joined in total."));

        if (plugin.adminManager.isAdmin(event.getPlayer().getUniqueId().toString())) {
            Admin admin = Admin.fromUuid(event.getPlayer().getUniqueId().toString());
            
            if (ValidationUtils.exists(admin.getLoginMessage())) {
                Bukkit.broadcastMessage(ChatUtils.colorize(admin.getLoginMessage()));
            }
            else {
                String rank = admin.getRank().toString().toLowerCase();
                Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.AQUA, admin.getPlayerName(), " is ", ("aeiou".indexOf(rank.charAt(0)) >= 0 ? "an" : "a"), WordUtils.capitalize(rank)));
            }
        }
        
        plugin.warnModule.runCheck(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
        for (Admin admin : plugin.adminManager.getAdmins()) {
            if (admin.hasCommandViewer()) {
                if (admin.getPlayer() != event.getPlayer()) {
                    admin.getPlayer().sendMessage(ChatColor.GRAY + ChatColor.stripColor(event.getPlayer().getName() + ": " + event.getMessage()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.adminManager.getToggledAdminChat().contains(event.getPlayer())) {
            com.wickedgaminguk.tranxcraft.util.ChatUtils.sendAdminChatMessage(event.getPlayer().getName(), event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        Player player = event.getPlayer();

        RewardPlayerData playerData = RewardPlayerData.getPlayerData(player);
    }
}
