package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
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

        LoggerUtils.info(plugin, StrUtils.concatenate("Found ", moduleSet.size(), " modules."));
        
        //Prioritize SQL Module as others depend on it.
        if (loadModule(SqlModule.class)) {
            LoggerUtils.info(plugin, "Loaded Module SqlModule successfully");
        }
        else {
            LoggerUtils.severe(plugin, "Issue loading module");
        }

        for (Class<? extends Module> module : moduleSet) {
            if (!this.modules.containsKey(module.getSimpleName())) {
                LoggerUtils.info(plugin, StrUtils.concatenate("Loading module ", module.getSimpleName()));

                if (loadModule(module)) {
                    LoggerUtils.info(plugin, StrUtils.concatenate("Loaded Module ", module.getSimpleName(), " successfully"));
                }
                else {
                    LoggerUtils.severe(plugin, "Issue loading module");
                }
            }
        }
    }

    public boolean loadModule(Class<? extends Module> module) {
        try {
            Constructor[] constructors = module.getConstructors();
            Constructor<?> constructor = null;
            
            if (constructors.length > 1) {
                constructor = module.getConstructor(TranxCraft.class);
            }
            
            Module mod = (constructor != null ? (Module) constructor.newInstance(plugin) : module.newInstance());
            mod.setup(plugin);

            modules.put(mod.getClass().getSimpleName(), mod);
        }
        catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
            LoggerUtils.severe(plugin, StrUtils.concatenate("Error loading module ", module.getSimpleName(), " because: "));
            LoggerUtils.severe(ex);
            return false;
        }

        return true;
    }

    public void loadModules(File[] files) {
        LoggerUtils.info(plugin, StrUtils.concatenate("Found ", files.length, " external modules."));
        
        for (File file : files) {
            if (file.isDirectory()) {
                LoggerUtils.info(plugin, StrUtils.concatenate(file.getName(), " is a directory."));
                break;
            }
            else {
                LoggerUtils.info(plugin, StrUtils.concatenate("Loading module ", file.getName()));
                if (loadModule(file)) {
                    LoggerUtils.info(plugin, StrUtils.concatenate("Loaded Module ", file.getName(), " successfully"));
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
        catch (IOException | InvalidDescriptionException ex) {
            LoggerUtils.severe(plugin, ex);
            return false;
        }
    }
}
