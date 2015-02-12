package com.wickedgaminguk.tranxcraft.player;

import com.wickedgaminguk.tranxcraft.modules.ModuleLoader;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import com.wickedgaminguk.tranxcraft.util.FetcherUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class Admin {

    private String uuid;
    private String playerName;
    private String ip;
    private Rank rank;
    private String email;
    private boolean hasCommandViewer;
    private String loginMessage;

    /** Gets the UUID of the admin.
     * @return The UUID of the admin.
     */
    public String getUuid() {
        return uuid;
    }

    /** Gets the name of the admin.
     * @return The name of the admin.
     */
    public String getPlayerName() {
        return playerName;
    }

    /** Gets the IP address of the admin.
     * @return The IP address of the admin.
     */
    public String getIp() {
        return ip;
    }

    /** Gets the IP address of the admin.
     * @return The IP address of the admin in an InetAddress form.
     */
    public InetAddress getIpAddress() {
        try {
            return InetAddress.getByName(ip);
        }
        catch (UnknownHostException ex) {
            return null;
        }
    }

    /** Gets the rank of the admin.
     * @return The rank of the admin.
     */
    public Rank getRank() {
        return rank;
    }

    /** Gets the e-mail of the admin.
     * @return The e-mail of the admin.
     */
    public String getEmail() {
        return email;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getUuid()));
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    /** Sets the UUID of the admin.
     * @param uuid The UUID of the admin.
     * @return The instance of Admin.
     */
    public Admin setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /** Sets the name of the admin.
     * @param playerName The name of the admin.
     * @return The instance of Admin.
     */
    public Admin setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    /** Sets the IP of the admin.
     * @param ip The IP of the admin.
     * @return The instance of Admin.
     */
    public Admin setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    /**
     * @see setIp(String ip)
     */
    public Admin setIp(InetAddress ip) {
        setIp(ip.getHostAddress());
        return this;
    }

    /** Sets the rank of the admin.
     * @param rank The rank of the admin.
     * @return The instance of Admin.
     */
    public Admin setRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    /** Sets the e-mail of the admin.
     * @param email The e-mail of the admin.
     * @return The instance of Admin.
     */
    public Admin setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public boolean hasCommandViewer() {
        return this.hasCommandViewer;        
    }
    
    public void setCommandViewer(boolean mode) {
        this.hasCommandViewer = mode;
    }

    /** Uploads the data to the MySQL server.
     * 
     */
    public void save() {
        SqlModule sql = (SqlModule) ModuleLoader.getModule("SqlModule");
        sql.getDatabase().update("UPDATE `admins` SET `player` = ?, `ip` = ?, `rank` = ?, `email` = ? WHERE `uuid` = ?", getPlayerName(), getIp(), getRank().toString().toLowerCase(), getEmail());
    }
    
    @Override
    public String toString() {
        return getPlayerName();
    }

    /** Loads an Admin instance from cache.
     * @param name The name of the admin that you want to load.
     * @return The loaded Admin instance.
     * @throws NullPointerException If the admin isn't in cache, it will throw this error.
     */
    public static Admin fromName(String name) throws NullPointerException {
        return AdminManager.getAdminCache().get(FetcherUtils.fetchUuid(name));
    }

    /** Loads an Admin instance from cache.
     * @param uuid The UUID of the admin that you want to load.
     * @return The loaded Admin instance.
     * @throws NullPointerException If the admin isn't in cache, it will throw this error.
     */
    public static Admin fromUuid(String uuid) throws NullPointerException {
        return AdminManager.getAdminCache().get(uuid);
    }
}
