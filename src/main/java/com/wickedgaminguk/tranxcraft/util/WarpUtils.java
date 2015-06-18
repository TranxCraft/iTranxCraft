package com.wickedgaminguk.tranxcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarpUtils extends Util {

    public static String[] getWarpList() {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `warps_global`");

        try {
            if (!result.isBeforeFirst()) {
                return null;
            }

            List<String> warps = new ArrayList<>();

            while (result.next()) {
                warps.add(result.getString("name"));
            }

            String[] warpList = new String[warps.size()];

            return warps.toArray(warpList);
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
            return null;
        }
    }

    public static Location getGlobalWarp(String name) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT `position` FROM `warps_global` WHERE `name` = ?;", name);

        try {
            if (!result.isBeforeFirst()) {
                return null;
            }

            result.next();

            String[] position = result.getString("position").split(",");

            return new Location(Bukkit.getWorld(position[0]), Double.valueOf(position[1]), Double.valueOf(position[2]), Double.valueOf(position[3]));
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
            return null;
        }
    }

    public static void addWarp(String name, Location location) {
        String loc = StrUtils.concatenate(location.getWorld().getName(), ",", location.getX(), ",", location.getY(), ",", location.getZ());

        plugin.sqlModule.getDatabase().update("INSERT INTO `warps_global` (`name`, `position`) VALUES (?, ?) ON DUPLICATE KEY UPDATE position = ?;", name, loc, loc);
    }

    public static void delWarp(String name) {
        plugin.sqlModule.getDatabase().update("DELETE FROM `warps_global` WHERE `name` = ?;", name);
    }
}
