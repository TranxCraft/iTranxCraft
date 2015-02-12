package com.wickedgaminguk.tranxcraft.util;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.apache.commons.lang.exception.ExceptionUtils;
import java.util.logging.Level;

public class DebugUtils extends Util<TranxCraft> {
    
    private static boolean isEnabled;
    private static Level level;

    public DebugUtils(Level level) {
        this.level = level;
    }

    /** Tests to see if DebugUtils works in the current configuration.
     * 
     */
    public void test() {
        plugin.getLogger().log(level, "Testing DebugUtils.");
    }

    /** If debug mode is enabled, it will log the message.
     * @param message The message to be logged.
     */
    public void debug(Object message) {
        if (isEnabled()) {
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

    /** Checks to see if debug mode is enabled.
     * @return If it's enabled or not.
     */
    public static boolean isEnabled() {
        return isEnabled;
    }

    /** Sets debug mode on or off.
     * @param mode What mode do you want debugging in
     */
    public static void setEnabled(boolean mode) {
        isEnabled = mode;
    }
}
