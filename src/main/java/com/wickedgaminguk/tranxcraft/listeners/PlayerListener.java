package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {

    private TranxCraft plugin;

    public PlayerListener(TranxCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (plugin.util.HARD_BANS.containsKey(player.getUniqueId().toString())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(plugin.util.HARD_BANS.get(player.getUniqueId().toString())));
        }
        else if (plugin.banUtils.isBanned(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatUtils.colorize(plugin.banUtils.getBan(player.getUniqueId().toString()).buildBanReason()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.warnModule.runCheck(event.getPlayer());
    }
}
