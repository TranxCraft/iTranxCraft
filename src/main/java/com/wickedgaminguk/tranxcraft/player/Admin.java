package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.player.AdminManager.AdminType;
import java.util.HashMap;

public class Admin {
    
    private static HashMap<String, Admin> adminCache = new HashMap<>();
    
    private String uuid;
    private String playerName;
    private String ip;
    private AdminType rank;
    private String email;

    public String getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIp() {
        return ip;
    }

    public AdminType getRank() {
        return rank;
    }

    public String getEmail() {
        return email;
    }

    public Admin setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Admin setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public Admin setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Admin setRank(AdminType rank) {
        this.rank = rank;
        return this;
    }

    public Admin setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public void save() {
        SqlModule sql = (SqlModule) ModuleLoader.getModule("SqlModule");
        sql.getDatabase().update("UPDATE `admins` SET `player` = ?, `ip` = ?, `rank` = ?, `email` = ? WHERE `uuid` = ?", getPlayerName(), getIp(), getRank().toString().toLowerCase(), getEmail());
    }
    
    public static HashMap<String, Admin> getAdminCache() {
        return adminCache;
    }
}
