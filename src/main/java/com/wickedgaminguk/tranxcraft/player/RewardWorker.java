package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RewardWorker extends BukkitRunnable {

    private final TranxCraft plugin;
    private final Server server;

    private static Long lastRan = null;

    public RewardWorker(TranxCraft instance) {
        this.plugin = instance;
        this.server = plugin.getServer();

        LoggerUtils.info(plugin, "RewardWorker instance created successfully.");
    }

    @Override
    public void run() {
        lastRan = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            RewardPlayerData playerData = RewardPlayerData.getPlayerData(player);

            player.sendMessage(StrUtils.concatenate(ChatColor.AQUA, "You have earned: $", playerData.getToPay(), "[", playerData.getMinedOres(), " Ores", playerData.getMinedBlocks(), " Blocks", playerData.getMobsKilled(), " Mobs", playerData.getPlayersKilled(), " Players]"));

            playerData.pay();
        }
    }

    public static Long getLastRan() {
        return lastRan;
    }
}
