package com.wickedgaminguk.tranxcraft.util;

import net.pravian.bukkitlib.util.LoggerUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class NetworkUtils extends Util {

    public static void download(String url, File output, boolean verbose) {
        try {
            final URL website = new URL(url);

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            try (FileOutputStream fos = new FileOutputStream(output)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

            if (verbose) {
                LoggerUtils.info("Downloaded " + url + " to " + output.toString() + ".");
            }
        }
        catch (MalformedURLException ex) {
            DebugUtils.debug(1, ex);
        }
        catch (IOException ex) {
            DebugUtils.debug(1, ex);
        }
    }
}
