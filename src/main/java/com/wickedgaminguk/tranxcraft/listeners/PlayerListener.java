package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.util.BanUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.util.ChatUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener extends Listener<TranxCraft> {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (!event.getHostname().equalsIgnoreCase("play.tranxcraft.com:25565")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Please join on the following address\n" + ChatColor.GOLD + "play.tranxcraft.com");
        }
        else if (BanUtils.getHardBans().containsKey(player.getUniqueId().toString())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(BanUtils.getHardBans().get(player.getUniqueId().toString())));
        }
        else if (plugin.banManager.isBanned(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(plugin.banManager.getBan(player.getUniqueId().toString()).buildBanReason()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int playerCount = Integer.valueOf(plugin.sqlModule.getStatistic("playercount"));
        playerCount++;
        plugin.sqlModule.setStatistic("playercount", String.valueOf(playerCount));
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + playerCount + " players & " + plugin.sqlModule.getRowCount("players") + " unique players have joined in total.");
        
        if (plugin.adminManager.isAdmin(event.getPlayer().getUniqueId().toString())) {
            Admin admin = Admin.fromUuid(event.getPlayer().getUniqueId().toString());
            
            if (ValidationUtils.exists(admin.getLoginMessage())) {
                Bukkit.broadcastMessage(ChatUtils.colorize(admin.getLoginMessage()));
            }
            else {
                String rank = admin.getRank().toString().toLowerCase();
                Bukkit.broadcastMessage(ChatColor.AQUA + admin.getPlayerName() + " is " + ("aeiou".indexOf(rank.charAt(0)) >= 0 ? "an" : "a") + WordUtils.capitalize(rank));
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
}
