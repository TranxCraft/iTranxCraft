package com.wickedgaminguk.tranxcraft;

import org.bukkit.entity.Player;

public class Ban {
    
    private String uuid;
    private String player;
    private String admin;
    private String reason;
    private String ip;
    private String expiry;
    
    public Ban() {

    }
    
    public Ban(String uuid, String player, String admin, String reason, String ip, String expiry) {
        this.uuid = uuid;
        this.player = player;
        this.admin = admin;
        this.reason = reason;
        this.ip = ip;
        this.expiry = expiry;
    }
    
    public Ban(Player player, String admin, String reason, String expiry) {
        this.uuid = player.getUniqueId().toString();
        this.player = player.getName();
        this.admin = admin;
        this.reason = reason;
        this.ip = player.getAddress().getHostString();
        this.expiry = expiry;
    }
    
    public String getUuid() {
        return uuid;        
    }
    
    public String getPlayer() {
        return player;        
    }
    
    public String getAdmin() {
        return admin;        
    }
    
    public String getReason() {
        return reason;        
    }
    
    public String getIp() {
        return ip;
    }
    
    public String getExpiry() {
        return expiry;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
