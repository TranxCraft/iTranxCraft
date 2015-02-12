package com.wickedgaminguk.tranxcraft.util;

import org.bukkit.Bukkit;

public class MinecraftUtils extends Util {
    
    public static String getNmsVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static String getMinecraftVersion() {
        String version = getNmsVersion().replaceAll("_", ".");
        return version.substring(1, version.length() - 3);
    }
}
