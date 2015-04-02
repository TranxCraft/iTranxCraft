package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticManager extends BukkitRunnable {

    private final TranxCraft plugin;

    public StatisticManager(TranxCraft plugin) {
        this.plugin = plugin;

        LoggerUtils.info(plugin, "StatisticManager instance created successfully.");
    }

    //TODO: Change Map Type to one that allows duplicate entries. Added 21/03/2015
    private static ConcurrentHashMap<String, String[]> statisticQueue = new ConcurrentHashMap<>();

    @Override
    public void run() {
        LoggerUtils.info(plugin, "Running queue...");
        LoggerUtils.info(plugin, StrUtils.concatenate("Executing ", statisticQueue.size(), " queries."));

        HashMap<String, String[]> queue = new HashMap<>(statisticQueue);

        for (HashMap.Entry<String, String[]> statistic : queue.entrySet()) {
            plugin.sqlModule.getDatabase().update(statistic.getKey(), statistic.getValue());
            statisticQueue.remove(statistic.getKey(), statistic.getValue());
        }

        LoggerUtils.info(plugin, "Executed queries.");
    }

    public static void addStatistic(String query, String... params) {
        statisticQueue.put(query, params);
        DebugUtils.debug(1, "Added a query to the queue.");
    }
}
