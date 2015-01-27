package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.event.Listener;

public class Module {

    protected void register(Class<? extends Listener> listener, TranxCraft plugin) {
        plugin.listenerLoader.loadListener(listener);
    }
}
