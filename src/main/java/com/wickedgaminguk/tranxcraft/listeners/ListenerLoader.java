package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.plugin.PluginManager;
import org.reflections.Reflections;
import java.util.Set;

public class ListenerLoader {

    private TranxCraft plugin;
    private PluginManager pluginManager;

    public ListenerLoader(TranxCraft plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    /** Loads all listeners in the given package that implements the Listener interface.
     * @param pkg The package that contains all of the listener classes.
     */
    public void loadListeners(Package pkg) {
        Reflections listeners = new Reflections(pkg);

        Set<Class<? extends Listener>> listenerSet = listeners.getSubTypesOf(Listener.class);

        LoggerUtils.info(plugin, StrUtils.concatenate("Found ", listenerSet.size(), " listeners."));

        for (Class<? extends Listener> listener : listenerSet) {
            LoggerUtils.info(plugin, StrUtils.concatenate("Registering listener ", listener.getSimpleName()));
            loadListener(listener);
        }
    }

    /** Loads a listener class that implements the Listener interface.
     * @param listener The listener class to load.
     */
    public void loadListener(Class<? extends Listener> listener) {
        try {
            Listener tListener = listener.newInstance();
            tListener.setup(plugin);
            
            pluginManager.registerEvents(tListener, plugin);
            LoggerUtils.info(plugin, StrUtils.concatenate("Registered Listener ", listener.getSimpleName(), " successfully"));
        }
        catch (InstantiationException | IllegalAccessException ex) {
            LoggerUtils.severe(plugin, StrUtils.concatenate("Error registering listener ", listener.getSimpleName(), " because ", ex));
        }
    }
}
