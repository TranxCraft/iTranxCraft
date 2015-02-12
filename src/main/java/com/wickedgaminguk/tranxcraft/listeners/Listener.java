package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;

public abstract class Listener<T extends TranxCraft> implements org.bukkit.event.Listener {
    
    protected T plugin;
}
