package com.wickedgaminguk.tranxcraft.util;

public class StrUtils extends Util {
    
    public static String concatenate(Object... objects) {
        StringBuilder builder = new StringBuilder();

        for (Object object : objects) {
            builder.append(object);
        }

        return builder.toString();
    }
}
