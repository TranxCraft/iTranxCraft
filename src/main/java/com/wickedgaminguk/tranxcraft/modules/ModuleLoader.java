package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.reflections.Reflections;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {
    
    private TranxCraft plugin;
    
    public ModuleLoader (TranxCraft plugin) {
        this.plugin = plugin;
    }
    
    private static HashMap<String, Module> modules = new HashMap<>();
    
    public void loadModules(Package pkg) {
        Reflections modules = new Reflections(pkg);

        Set<Class<? extends Module>> moduleSet = modules.getSubTypesOf(Module.class);

        LoggerUtils.info(plugin, "Found " + moduleSet.size() + " modules.");

        for (Class<? extends Module> module : moduleSet) {
            LoggerUtils.info(plugin, "Loading module " + module.getSimpleName());

            if (loadModule(module)) {
                LoggerUtils.info("Loaded Module " + module.getSimpleName() + " successfully");
            }
            else {
                LoggerUtils.severe(plugin, "Issue loading module.");
            }
        }
    }
    
    public void loadModules(File[] files) {
        for (File file : files) {
            LoggerUtils.info(plugin, file.getName());
            if (file.isDirectory()) {
                LoggerUtils.info(plugin, file.getName() + " is a directory.");
                break;
            }
            else {
                LoggerUtils.info(plugin, "Loading Module...");
                loadModule(file);
            }
        }
    }
    
    public boolean loadModule(File file) {
        try {
            JarFile jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("module.yml");
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(jar.getInputStream(entry));
            Class<? extends Module> module;
            
            try {
                module = Class.forName(descriptionFile.getMain()).asSubclass(Module.class);
            }
            catch (ClassCastException ex) {
                LoggerUtils.info(ex.getMessage());
                return false;
            }
            catch (ClassNotFoundException ex) {
                LoggerUtils.info(ex.getMessage());
                return false;
            }
            
            if (!module.isAssignableFrom(Module.class)) {
                LoggerUtils.warning(plugin, "Module " + descriptionFile.getName() + " cannot be loaded. Main class does not extend Module.");
                return false;
            }
            else {
                loadModule(module);
                return true;
            }            
        }
        catch (IOException ex) {
            LoggerUtils.info(ex.getMessage());
            return false;
        }
        catch (InvalidDescriptionException ex) {
            LoggerUtils.info(ex.getMessage());
            return false;
        }
    }
    
    public boolean loadModule(Class<? extends Module> module) {
        try {
            Constructor<?> constructor = module.getConstructor(TranxCraft.class);
            Module mod = (Module) constructor.newInstance(plugin);
            
            modules.put(mod.getClass().getSimpleName(), mod);
        }
        catch(Exception ex) {
            LoggerUtils.severe(plugin, "Error loading module " + module.getSimpleName() + " because: " + ex);
            return false;
        }
        return true;
    }
    
    public static Module getModule(String name) {
        return modules.get(name);
    }
}
