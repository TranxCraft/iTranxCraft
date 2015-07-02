package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.NumberUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AnnouncementModule extends Module<TranxCraft> {

    private BukkitScheduler scheduler;

    private static HashMap<String, Integer> announcements;
    private static SqlModule sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");

    @Override
    public void onLoad() {
        scheduler = Bukkit.getServer().getScheduler();
        announcements = new HashMap<>();

        loadAnnouncements();
        scheduleAnnouncements();

        new BukkitRunnable() {
            @Override
            public void run() {
                reloadAnnouncements();
            }
        }.runTaskTimer(plugin, 900 * 20L, 900 * 20L);
    }

    public void loadAnnouncements() {
        LoggerUtils.info(plugin, "Loading announcements.");
        DebugUtils.debug(3, "SELECT * FROM `announcements`;");

        ResultSet result = sqlModule.getDatabase().query("SELECT * FROM `announcements`;");

        try {
            DebugUtils.debug(2, DebugUtils.resultSetToJSON(result));

            while (result.next()) {
                String announcement = result.getString("announcement");
                String interval = result.getString("interval");

                if (!ValidationUtils.exists(announcement, interval)) {
                    DebugUtils.debug(2, "Not loading announcement, either the announcement text or the interval are empty.");
                    return;
                }

                if (!NumberUtils.isInt(interval)) {
                    DebugUtils.debug(2, "Not loading announcement, the interval is not a valid integer.");
                    return;
                }

                announcements.put(announcement, Integer.valueOf(interval));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
        }
    }

    public void reloadAnnouncements() {
        LoggerUtils.info(plugin, "Reloading announcements.");

        announcements.clear();
        scheduler.cancelAllTasks();

        loadAnnouncements();
        scheduleAnnouncements();
    }

    public void scheduleAnnouncements() {
        for (HashMap.Entry<String, Integer> announcement : announcements.entrySet()) {
            long interval = announcement.getValue() * 20L;

            scheduler.runTaskTimer(plugin, () -> sendAnnouncement(announcement.getKey()), interval, interval);
        }
    }

    public void sendAnnouncement(String announcement) {
        if (announcement.contains("{%player%}")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatUtils.colorize(announcement.replace("{%player%}", player.getName())));
            }
        }
        else {
            Bukkit.broadcastMessage(ChatUtils.colorize(announcement));
        }
    }

    public static void addAnnouncement(String name, String announcement, int interval) {
        sqlModule.getDatabase().update("INSERT INTO `announcements` (`name`, `announcement`, `interval`) VALUES (?, ?, ?);", name, announcement, String.valueOf(interval));
    }

    public static void removeAnnouncement(String name) {
        sqlModule.getDatabase().update("DELETE FROM `announcements` WHERE `name` = ?;", name);
    }

    public static HashMap<String, Integer> getAnnouncements(String... names) {
        if (names[0].equals("*")) {
            return announcements;
        }

        HashMap<String, Integer> announcements = new HashMap<>();

        for (String name : names) {
            try {
                ResultSet result = sqlModule.getDatabase().query("SELECT * FROM `announcements` WHERE `name` = ?;", name);

                if (result == null) {
                    return null;
                }

                if (!result.isBeforeFirst()) {
                    return null;
                }

                result.next();

                announcements.put(result.getString("announcement"), result.getInt("interval"));
            }
            catch (SQLException ex) {
                DebugUtils.debug(ex);
            }
        }

        return announcements;
    }
    
    public static int getAnnouncementCount() {
        return announcements.size();
    }
}
