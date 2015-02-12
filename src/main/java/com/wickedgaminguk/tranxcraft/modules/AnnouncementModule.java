package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.NumberUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AnnouncementModule extends Module<TranxCraft> {
    
    private SqlModule sqlModule;
    private static HashMap<String, Integer> announcements = new HashMap<>();
    
    public AnnouncementModule() {
        this.sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
        
        ResultSet result = sqlModule.getDatabase().query("SELECT * FROM `announcements`");

        try {
            LoggerUtils.info(plugin, "Loading announcements.");
            
            while (result.next()) {                
                String announcement = result.getString("announcement");
                String interval = result.getString("interval");
                
                if (!ValidationUtils.exists(announcement, interval)) {
                    return;    
                }
                
                if (NumberUtils.isInt(interval)) {
                    return;
                }
                
                announcements.put(result.getString("announcement"), Integer.valueOf(result.getString("interval")));
            }
        }
        catch (SQLException ex) {
            plugin.debugUtils.debug(ex);
        }
        
        LoggerUtils.info(plugin, "Loaded " + announcements.size() + " announcements.");
        scheduleAnnouncements();
    }
    
    public void scheduleAnnouncements() {
        for (HashMap.Entry<String, Integer> announcement : announcements.entrySet()) {
            long interval = announcement.getValue() * 20L;
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(ChatUtils.colorize(announcement.getKey()));
                }
            }.runTaskTimer(plugin, interval, interval);
        }
    }
    
    public static HashMap<String, Integer> getAnnouncements() {
        return announcements;
    }
}
