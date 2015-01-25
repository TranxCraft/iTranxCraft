package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;

public class ListenerLoader {
    
    private TranxCraft plugin;
    private PluginManager pluginManager;
    
    public ListenerLoader(TranxCraft plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
    }
    
    public void loadListeners(Package pkg) {
        Reflections listeners = new Reflections(pkg);

        Set<Class<? extends Listener>> listenerSet = listeners.getSubTypesOf(Listener.class);

        LoggerUtils.info(plugin, "Found " + listenerSet.size() + " listeners.");

        for (Class<? extends Listener> listener : listenerSet) {
            LoggerUtils.info(plugin, "Registering listener " + listener.getSimpleName());
            loadListener(listener);
        }
    }
    
    public void loadListener(Class<? extends Listener> listener) {
        try {
            Constructor<?> constructor = listener.getConstructor(TranxCraft.class);
            pluginManager.registerEvents((Listener) constructor.newInstance(plugin), plugin);
            LoggerUtils.info("Registered Listener " + listener.getSimpleName() + " successfully");
        }
        catch (Exception ex) {
            LoggerUtils.severe(plugin, "Error registering listener " + listener.getSimpleName() + " because " + ex);
        }
    }
}
