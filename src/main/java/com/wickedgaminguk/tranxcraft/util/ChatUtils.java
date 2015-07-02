package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.player.AdminManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatUtils extends Util {

    public static void sendAdminChatMessage(String sender, String message) {
        Bukkit.getOnlinePlayers().stream().filter(player -> AdminManager.isAdmin(player.getUniqueId().toString())).forEach(player -> player.sendMessage(net.pravian.bukkitlib.util.ChatUtils.colorize(StrUtils.concatenate("&f[&bADMIN&f]&4 ", sender, ": &b", message))));
    }
}
