package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class StatisticManager extends BukkitRunnable {

    private final TranxCraft plugin;

    public StatisticManager(TranxCraft plugin) {
        this.plugin = plugin;

        LoggerUtils.info(plugin, "StatisticManager instance created successfully.");
    }
    
    private static List<PreparedStatement> queue = new CopyOnWriteArrayList<>();

    @Override
    public void run() {
        List<PreparedStatement> toRun = Collections.synchronizedList(queue);

        LoggerUtils.info(plugin, "Running queue...");
        LoggerUtils.info(plugin, StrUtils.concatenate("Executing ", toRun.size(), " queries."));

        for (PreparedStatement statement : toRun) {
            plugin.sqlModule.execute(statement);
            queue.remove(statement);
        }

        LoggerUtils.info(plugin, "Executed queries.");
    }

    public static void addStatistic(PreparedStatement statement) {
        queue.add(statement);
        DebugUtils.debug(1, "Added a query to the queue.");
    }
}
