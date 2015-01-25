package com.wickedgaminguk.tranxcraft.utils;

import com.wickedgaminguk.tranxcraft.TranxCraft;

import java.util.logging.Level;

public class LogUtils {
    
    private TranxCraft plugin;
    private boolean isEnabled;
    private Level level;
    
    public LogUtils(TranxCraft plugin, Level level) {
        this.plugin = plugin;
        this.level = level;
    }
    
    public void debug(String message) {
        if (isEnabled) {
            plugin.getLogger().log(level, message);
        }
    }
}
