package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.Admin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtils extends Util {
    
    public static void sendAdminChatMessage(Admin sender, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.adminManager.isAdmin(player.getUniqueId().toString())) {
                player.sendMessage(net.pravian.bukkitlib.util.ChatUtils.colorize(StrUtils.concatenate("&f[&bADMIN&f]&b ", sender.getPlayerName(), ": &b ", message)));
            }
        }
    }
}
