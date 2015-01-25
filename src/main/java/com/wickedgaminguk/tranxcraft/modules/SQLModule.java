package com.wickedgaminguk.tranxcraft.modules;

import com.visionwarestudios.database.mysql.MySQL;
import com.wickedgaminguk.tranxcraft.TranxCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlModule extends Module {

    private TranxCraft plugin;
    private MySQL database ;

    public SqlModule(TranxCraft plugin) {
        this.plugin = plugin;
        this.database = plugin.database;
    }
    
    public boolean isInitialized() {
        return false;
    }
    
    public void initialize() {
    }
    
    public MySQL getDatabase() {
        return this.database;
    }
    
    public String getConfigEntry(String entry) {
        ResultSet response = getDatabase().query("SELECT * FROM `config` WHERE `config` = ?", entry);
        
        try {
            response.next();
            return response.getString("entry");            
        }
        catch (SQLException ex) {
            plugin.logUtils.debug(ex.getMessage());
            return null;
        }
    }
    
    public boolean getBoolean(String entry) {
        String response = getConfigEntry(entry);
        
        return response.equals("1");
    }
    
    public int getInt(String entry) {
        return Integer.valueOf(getConfigEntry(entry));        
    }
    
    public double getDouble(String entry) {
        return Double.valueOf(getConfigEntry(entry));
    }
}
