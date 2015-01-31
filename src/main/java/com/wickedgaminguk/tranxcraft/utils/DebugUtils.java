package com.wickedgaminguk.tranxcraft.utils;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.apache.commons.lang.exception.ExceptionUtils;
import java.util.logging.Level;

public class DebugUtils {

    private TranxCraft plugin;
    private boolean isEnabled;
    private Level level;

    public DebugUtils(TranxCraft plugin, Level level) {
        this.plugin = plugin;
        this.level = level;
    }
    
    public void test() {
        plugin.getLogger().log(level, "Testing DebugUtils.");
    }

    public void debug(Object message) {
        if (isEnabled) {
            final String line;

            if (message instanceof Throwable) {
                line = ExceptionUtils.getStackTrace((Throwable) message);
            }
            else {
                line = String.valueOf(message);
            }
            
            plugin.getLogger().log(level, line);
        }
    }
}
