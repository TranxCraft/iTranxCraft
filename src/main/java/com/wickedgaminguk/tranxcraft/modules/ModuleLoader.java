package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.reflections.Reflections;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    private static HashMap<String, Module> modules = new HashMap<>();
    
    private TranxCraft plugin;
    private JarClassLoader classLoader = new JarClassLoader();
    private JclObjectFactory factory = JclObjectFactory.getInstance();

    public ModuleLoader(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public static Module getModule(String name) {
        return modules.get(name);
    }
    
    public static int getModuleCount() {
        return modules.size();
    }
    
    public void loadModules(Package pkg) {
        Reflections modules = new Reflections(pkg);

        Set<Class<? extends Module>> moduleSet = modules.getSubTypesOf(Module.class);

        LoggerUtils.info(plugin, "Found " + moduleSet.size() + " modules.");
        
        //Prioritize SQL Module as others depend on it.
        if (loadModule(SqlModule.class)) {
            LoggerUtils.info(plugin, "Loaded Module SqlModule successfully");
        }
        else {
            LoggerUtils.severe(plugin, "Issue loading module");
        }

        for (Class<? extends Module> module : moduleSet) {
            if (this.modules.containsKey(module.getSimpleName())) {
                return;
            }
            
            LoggerUtils.info(plugin, "Loading module " + module.getSimpleName());

            if (loadModule(module)) {
                LoggerUtils.info(plugin, "Loaded Module " + module.getSimpleName() + " successfully");
            }
            else {
                LoggerUtils.severe(plugin, "Issue loading module");
            }
        }
    }

    public boolean loadModule(Class<? extends Module> module) {
        try {
            Constructor<?> constructor = module.getConstructor(TranxCraft.class);
            Module mod = (Module) constructor.newInstance(plugin);

            modules.put(mod.getClass().getSimpleName(), mod);
        }
        catch (InvocationTargetException ex) {
            LoggerUtils.severe(plugin, "Error loading module " + module.getSimpleName() + " because: ");
            LoggerUtils.severe(ex);
            return false;
        }
        catch (NoSuchMethodException ex) {
            LoggerUtils.severe(plugin, "Error loading module " + module.getSimpleName() + " because: ");
            LoggerUtils.severe(ex);
            return false;
        }
        catch (InstantiationException ex) {
            LoggerUtils.severe(plugin, "Error loading module " + module.getSimpleName() + " because: ");
            LoggerUtils.severe(ex);
            return false;
        }
        catch (IllegalAccessException ex) {
            LoggerUtils.severe(plugin, "Error loading module " + module.getSimpleName() + " because: ");
            LoggerUtils.severe(ex);
            return false;
        }

        return true;
    }

    public void loadModules(File[] files) {
        LoggerUtils.info(plugin, "Found " + files.length + " external modules.");
        
        for (File file : files) {
            if (file.isDirectory()) {
                LoggerUtils.info(plugin, file.getName() + " is a directory.");
                break;
            }
            else {
                LoggerUtils.info(plugin, "Loading module " + file.getName());
                if (loadModule(file)) {
                    LoggerUtils.info(plugin, "Loaded Module " + file.getName() + " successfully");
                }
                else {
                    LoggerUtils.severe(plugin, "Issue loading module");
                }
            }
        }
    }

    public boolean loadModule(File file) {
        try {
            JarFile jar = new JarFile(file);
            
            classLoader.add(file.getAbsolutePath());
            
            JarEntry entry = jar.getJarEntry("module.yml");

            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(jar.getInputStream(entry));

            Object object = factory.create(classLoader, descriptionFile.getMain(), plugin);
            
            Module module = (Module) object;
            
            modules.put(descriptionFile.getMain(), module);
            
            return true;
        }
        catch (IOException ex) {
            LoggerUtils.severe(plugin, ex);
            return false;
        }
        catch (InvalidDescriptionException ex) {
            LoggerUtils.severe(plugin, ex);
            return false;
        }
    }
}
