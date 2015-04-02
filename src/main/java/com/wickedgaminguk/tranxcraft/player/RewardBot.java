package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RewardBot extends BukkitRunnable {

    private final List<ItemStack> rewardPool = new ArrayList<>();
    private final TranxCraft plugin;
    private final Server server;

    private Random random = new Random();

    private static Long lastRan = null;
    private static int runCounter;

    public RewardBot(TranxCraft instance) {
        this.plugin = instance;
        this.server = plugin.getServer();

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `reward_pool`");

        try {
            while (result.next()) {
                rewardPool.add(new ItemStack(Material.valueOf(result.getString("reward").toUpperCase())));
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(ex);
        }

        LoggerUtils.info(plugin, "RewardBot instance created successfully.");
    }

    @Override
    public void run() {
        lastRan = System.currentTimeMillis();
        runCounter++;

        Collection<? extends Player> onlinePlayers = server.getOnlinePlayers();

        int onlinePlayerCount = onlinePlayers.size();
        int randomPlayer;

        randomPlayer = random.nextInt(onlinePlayerCount + 10);

        if (runCounter % 7.5 == 0) {
            server.broadcastMessage(StrUtils.concatenate(ChatColor.GREEN, ChatColor.BOLD, "It's that time again... For prizes!"));

            for (Player player : server.getOnlinePlayers()) {
                sendReward(player, getRandomReward());
            }
        }

        if (randomPlayer >= onlinePlayerCount) {
            Player player = (Player) onlinePlayers.toArray()[randomPlayer];
            ItemStack randomItem = getRandomReward();

            server.broadcastMessage(StrUtils.concatenate(ChatColor.GREEN, "Congratulations to ", player.getName(), " for being randomly selected to win ", randomItem.getType().toString().toLowerCase(), "!"));
            sendReward(player, randomItem);
        }
    }

    private void sendReward(Player player, ItemStack reward) {
        final int firstEmpty = player.getInventory().firstEmpty();

        if (firstEmpty >= 0) {
            player.getInventory().setItem(firstEmpty, reward);
            player.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Congratulations for being randomly selected to win ", reward.getType().toString().toLowerCase(), "!"));
        }
    }

    private ItemStack getRandomReward() {
        ItemStack randomItem = rewardPool.get(random.nextInt(rewardPool.size()));

        return randomItem;
    }
}
