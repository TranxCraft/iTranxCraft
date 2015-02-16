package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.TranxCraft;

public class UtilManager {
    
    private static TranxCraft plugin;
    
    public UtilManager(TranxCraft plugin) {
        this.plugin = plugin;
    }
    
    public static TranxCraft getPlugin() {
        return plugin;
    }
}
