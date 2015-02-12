package com.wickedgaminguk.tranxcraft.util;

public class NumberUtils extends Util {

    /** Checks if a string is a valid integer.
     * @param integer The string that you want to check.
     * @return Whether it's a valid integer or not.
     */
    public static boolean isInt(String integer) {
        try {
            Integer.parseInt(integer);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        
        return true;        
    }

    /** Checks if a string is a valid double.
     * @param number The string that you want to check.
     * @return Whether it's a valid double or not.
     */
    public static boolean isDouble(String number) {
        try {
            Double.parseDouble(number);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        
        return true;
    }

    /** Checks if a string is a valid float.
     * @param number The string that you want to check.
     * @return Whether it's a valid float or not.
     */
    public static boolean isFloat(String number) {
        try {
            Float.parseFloat(number);
        }
        catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
