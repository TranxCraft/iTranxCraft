package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

//Credits to https://github.com/DarthCraft/DarthCraft/blob/master/src/net/darthcraft/dcmod/addons/BanWarner.java
public class WarnModule extends Module<TranxCraft> {

    public void runCheck(Player player) {
        getFishbansRunnable(player).runTaskAsynchronously(plugin);
    }

    public BukkitRunnable getFishbansRunnable(final Player player) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                final URL url = getUrl(player);
                final JSONObject json;

                try {
                    final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    json = (JSONObject) JSONValue.parse(in.readLine());
                    in.close();
                }
                catch (Exception ex) {
                    plugin.debugUtils.debug("Error fetching fishbans information from " + url.getHost());
                    plugin.debugUtils.debug(ex.getMessage());
                    return;
                }

                plugin.debugUtils.debug(url.toString());
                plugin.debugUtils.debug(json.toJSONString());

                getWarnRunnable(json).runTask(plugin);
            }
        };
    }

    public URL getUrl(Player player) {
        try {
            return new URL("http://api.fishbans.com/stats/" + player.getName());
        }
        catch (MalformedURLException ex) {
            LoggerUtils.warning("Could not generate fishbans URL for " + player.getName());
            return null;
        }
    }

    public BukkitRunnable getWarnRunnable(final JSONObject object) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (object.get("success").equals(false)) {
                        plugin.debugUtils.debug("Fishbans returned success: false");
                        plugin.debugUtils.debug(object.get("error").toString());
                        return;
                    }

                    final JSONObject stats = (JSONObject) object.get("stats");

                    if (stats.get("totalbans").equals(0L)) {
                        return;
                    }

                    /* TODO: AdminChat
                    plugin.adminChat.sendAdminMessage("BanWarner", ChatColor.RED + "Warning: " + stats.get("username") + " has been banned " + stats.get("totalbans") + " times!");
                    */

                    final JSONObject services = (JSONObject) stats.get("service");

                    for (Object service : services.keySet()) {
                        if (services.get(service).equals(0L)) {
                            continue;
                        }
                        /* TODO: AdminChat
                        plugin.adminChat.sendAdminMessage("BanWarner", ChatColor.RED + "Warning: " + services.get(service) + " times on " + service);
                        */
                    }
                }
                catch (Exception ex) {
                    plugin.debugUtils.debug("Error parsing fishbans JSON: " + object);
                    plugin.debugUtils.debug(ex.toString());
                }
            }
        };
    }
}