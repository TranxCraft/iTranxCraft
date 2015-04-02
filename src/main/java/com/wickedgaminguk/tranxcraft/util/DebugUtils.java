package com.wickedgaminguk.tranxcraft.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class DebugUtils extends Util {
    
    private static boolean isEnabled;
    private static int debugLevel = 1;
    private static Level level = Level.INFO;

    public static void debug(int level, Object message) {
            if (debugLevel >= level) {
                debug(message);
            }
    }

    /** If debug mode is enabled, it will log the message.
     * @param message The message to be logged.
     */
    public static void debug(Object message) {
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
    
    public static void setLevel(Level lvl) {
        level = lvl;
    }
    
    public static Level getLevel() {
        return level;
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

    public static void setDebugLevel(int level) {
        debugLevel = level;
    }

    public static int getDebugLevel() {
        return debugLevel;
    }

    public static String resultSetToJSON(ResultSet result) {
        JSONObject json = new JSONObject();

        try {
            result.beforeFirst();

            if (!result.isBeforeFirst()) {
                json.put("Error", "Result from query is empty.");
                return json.toJSONString();
            }

            int columnCount = result.getMetaData().getColumnCount();
            int rowCount = 0;

            while (result.next()) {
                rowCount++;
            }

            debug(columnCount);

            result.beforeFirst();

            JSONArray info = new JSONArray();

            while (result.next()) {
                if (rowCount == 1) {
                    for (int i = 1; i <= columnCount; i++) {
                        json.put(result.getMetaData().getColumnName(i), result.getString(i));
                    }
                }
                else {
                    JSONObject object = new JSONObject();

                    for (int i = 1; i <= columnCount; i++) {
                        object.put(result.getMetaData().getColumnName(i), result.getString(i));
                    }

                    info.add(object);
                }
            }

            if (rowCount != 1) {
                json.put("announcements", info);
            }

            result.beforeFirst();
        }
        catch (SQLException ex) {
            debug(1, ex);
        }

        return json.toJSONString();
    }
}
