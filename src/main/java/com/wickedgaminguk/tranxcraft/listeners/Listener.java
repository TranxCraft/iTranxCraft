package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;

public abstract class Listener<T extends TranxCraft> implements org.bukkit.event.Listener {
    
    protected T plugin;
    
    protected void setup(T plugin) {
        this.plugin = plugin;
        onLoad();
    }
    
    protected void onLoad() {
                
    }
}
