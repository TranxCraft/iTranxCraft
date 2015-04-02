package com.wickedgaminguk.tranxcraft.player;

import org.bukkit.entity.Player;
import java.util.HashMap;

public class RewardPlayerData {

    private static HashMap<Player, RewardPlayerData> playerData = new HashMap<>();

    private double toPay = 0;
    private int minedOres = 0;
    private int minedBlocks = 0;
    private int mobsKilled = 0;
    private int playersKilled = 0;
    private long lastReward = 0;

    private RewardPlayerData(Player player) {

    }

    public long getLastReward() {
        return lastReward;
    }

    public void setLastReward(long lastReward) {
        this.lastReward = lastReward;
    }

    public int getPlayersKilled() {
        return playersKilled;
    }

    public void setPlayersKilled(int playersKilled) {
        this.playersKilled = playersKilled;
    }

    public int getMobsKilled() {
        return mobsKilled;
    }

    public void setMobsKilled(int mobsKilled) {
        this.mobsKilled = mobsKilled;
    }

    public int getMinedBlocks() {
        return minedBlocks;
    }

    public void setMinedBlocks(int minedBlocks) {
        this.minedBlocks = minedBlocks;
    }

    public int getMinedOres() {
        return minedOres;
    }

    public void setMinedOres(int minedOres) {
        this.minedOres = minedOres;
    }

    public double getToPay() {
        return toPay;
    }

    public void setToPay(double toPay) {
        this.toPay = toPay;
    }

    public void pay() {
        lastReward = System.currentTimeMillis();

        toPay = 0;
        minedOres = 0;
        minedBlocks = 0;
        mobsKilled = 0;
        playersKilled = 0;
    }

    public static RewardPlayerData getPlayerData(Player player) {
        if (playerData.containsKey(player)) {
            return playerData.get(player);
        }

        RewardPlayerData rewardPlayerData = new RewardPlayerData(player);
        playerData.put(player, rewardPlayerData);

        return rewardPlayerData;
    }
}
