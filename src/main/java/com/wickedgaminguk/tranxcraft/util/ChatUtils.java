package com.wickedgaminguk.tranxcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtils extends Util {
    
    public static void sendAdminChatMessage(String sender, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.adminManager.isAdmin(player.getUniqueId().toString())) {
                player.sendMessage(net.pravian.bukkitlib.util.ChatUtils.colorize(StrUtils.concatenate("&f[&bADMIN&f]&b ", sender, ": &b ", message)));
            }
        }
    }
}
