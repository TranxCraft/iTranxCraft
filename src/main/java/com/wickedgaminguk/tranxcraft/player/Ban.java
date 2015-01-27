package com.wickedgaminguk.tranxcraft.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String buildBanReason() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.ofEpochSecond(Long.valueOf(getExpiry()))));
        return ChatColor.RED + "You have been banned by " + getAdmin() + "\nfor " + getReason() + "\nThis ban expires on the " + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR);
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
