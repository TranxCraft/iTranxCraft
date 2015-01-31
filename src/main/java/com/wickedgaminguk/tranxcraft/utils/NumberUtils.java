package com.wickedgaminguk.tranxcraft.utils;

public class NumberUtils {
    
    public static boolean isInt(String integer) {
        try {
            Integer.parseInt(integer);
        }
        catch (NumberFormatException e) {
            return false;
        }
        
        return true;        
    }
}
