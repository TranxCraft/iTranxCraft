package com.wickedgaminguk.tranxcraft.util;

public class StrUtils extends Util {
    
    public static String concatenate(Object... objects) {
        StringBuilder builder = new StringBuilder();

        for (Object object : objects) {
            builder.append(object);
        }

        return builder.toString();
    }

    public static String removeWhitespace(String string) {
        if (string != null && !string.isEmpty()) {
            return string.replaceAll("\\s+", "");
        }
        else {
            return string;
        }
    }
}
