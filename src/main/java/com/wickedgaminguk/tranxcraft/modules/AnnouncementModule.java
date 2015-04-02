package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.NumberUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AnnouncementModule extends Module<TranxCraft> {
    
    private SqlModule sqlModule;
    private BukkitScheduler scheduler;

    private static HashMap<String, Integer> announcements;
    
    @Override
    public void onLoad() {
        sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
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
            plugin.debugUtils.debug(ex);
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

            scheduler.runTaskTimer(plugin, () -> Bukkit.broadcastMessage(ChatUtils.colorize(announcement.getKey())), interval, interval);
        }
    }
    
    public static int getAnnouncementCount() {
        return announcements.size();
    }
}
