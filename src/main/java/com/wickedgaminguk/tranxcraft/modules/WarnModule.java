package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.ChatUtils;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.ChatColor;
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
                    DebugUtils.debug(StrUtils.concatenate("Error fetching fishbans information from ", url.getHost()));
                    DebugUtils.debug(ex);
                    return;
                }

                getWarnRunnable(json).runTask(plugin);
            }
        };
    }

    public URL getUrl(Player player) {
        try {
            return new URL(StrUtils.concatenate("http://api.fishbans.com/stats/", player.getName()));
        }
        catch (MalformedURLException ex) {
            LoggerUtils.warning(StrUtils.concatenate("Could not generate fishbans URL for ", player.getName()));
            return null;
        }
    }

    public BukkitRunnable getWarnRunnable(final JSONObject object) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (object.get("success").equals(false)) {
                        DebugUtils.debug("Fishbans returned success: false");
                        DebugUtils.debug(object.get("error").toString());
                        return;
                    }

                    final JSONObject stats = (JSONObject) object.get("stats");

                    if (stats.get("totalbans").equals(0L)) {
                        return;
                    }

                    ChatUtils.sendAdminChatMessage("WarnModule", StrUtils.concatenate(ChatColor.RED, "Warning: ",  stats.get("username"), " has been banned ", stats.get("totalbans"), " times!"));

                    final JSONObject services = (JSONObject) stats.get("service");

                    for (Object service : services.keySet()) {
                        if (services.get(service).equals(0L)) {
                            continue;
                        }

                        ChatUtils.sendAdminChatMessage("WarnModule", StrUtils.concatenate(ChatColor.RED, "Warning: ", services.get(service), " times on ", service));
                    }
                }
                catch (Exception ex) {
                    DebugUtils.debug(StrUtils.concatenate("Error parsing fishbans JSON: ", object));
                    DebugUtils.debug(ex);
                }
            }
        };
    }
}