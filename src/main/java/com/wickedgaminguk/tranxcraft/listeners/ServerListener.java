package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener extends Listener<TranxCraft> {
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPing(ServerListPingEvent event) {
        TranxPlayer player = TranxPlayer.loadFromSql(plugin, event.getAddress());
        
        if (player != null) {
            if (plugin.banManager.isBanned(player.getUuid())) {
                event.setMotd(ChatColor.RED + "Sorry " + player + ", but you are" + ChatColor.BOLD + " banned.");
            }
            /*TODO: Staff Mode
            else if (plugin.util.isStaffMode() == true && !(player.isAdmin())) {
                event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, adminmode is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
            }
            */
            else if (Bukkit.hasWhitelist() && Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(player.getName())) == false) {
                event.setMotd(ChatColor.RED + "Sorry " + player + ", but the whitelist is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
            }
            else if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() && !(player.isAdmin())) {
                event.setMotd(ChatColor.RED + "Sorry " + player + ", but the server is full - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
            }
            else {
                event.setMotd(ChatColor.GREEN  + "Welcome back to " + ChatColor.WHITE + "TranxCraft, " + ChatColor.GREEN + player.getName());
            }
        }
        else if (plugin.banManager.isBanned(event.getAddress())) {
            event.setMotd(ChatColor.RED + "You are banned.");
        }
        /*TODO: Staff Mode
        else if (plugin.util.isStaffMode() == true) {
            event.setMotd(ChatColor.RED + "Adminmode enabled.");
        }
        */
        else if (Bukkit.hasWhitelist()) {
            event.setMotd(ChatColor.RED + "The whitelist is enabled.");
        }
        else if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            event.setMotd(ChatColor.RED + "The Server is full.");
        }
        else {
            event.setMotd(ChatColor.GREEN + "TranxCraft" + ChatColor.WHITE + " - " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftUtils.getMinecraftVersion());
        }
    }
}
