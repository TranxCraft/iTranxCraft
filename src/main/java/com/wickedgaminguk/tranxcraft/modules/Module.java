package com.wickedgaminguk.tranxcraft.modules;

import org.bukkit.plugin.Plugin;

public abstract class Module<T extends Plugin> {
    
    protected T plugin;
    
    protected void setup(T plugin) {
        this.plugin = plugin;
    }
}
