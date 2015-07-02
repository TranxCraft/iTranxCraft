package com.wickedgaminguk.tranxcraft.util;

import org.bukkit.plugin.Plugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class WriteUtils extends Util {

    private static void write(String message) {
        try {
            File file = new File(plugin.getDataFolder() + "/debug.log");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);

            try (BufferedWriter bw = new BufferedWriter(fw)) {
                if (file.length() != 0) {
                    bw.newLine();
                }

                bw.write(message);
            }
        }
        catch (IOException ex) {

        }
    }

    public static void log(Level level, Plugin plugin, String message) {
        write("[" + getDate() + "] " + "[" + plugin.getName() + "] [" + level.getName() + "] " + message);
    }

    private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String date = sdf.format(new Date());
        return date;
    }
}
