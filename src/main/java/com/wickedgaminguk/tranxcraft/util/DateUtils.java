package com.wickedgaminguk.tranxcraft.util;

public class DateUtils extends Util {

    public static String parseMs(int ms) {
        if (ms >= 1000 && ms < 60000) {
            return StrUtils.concatenate(ms / 1000, " seconds");
        }
        else if (ms >= 60000 && ms < 3600000) {
            return StrUtils.concatenate(ms / 60000, " minutes");
        }
        else if (ms >= 3600000 && ms < 86400000) {
            return StrUtils.concatenate(ms / 3600000, " hours");
        }
        else if (ms >= 86400000 && ms < 604800000) {
            return StrUtils.concatenate(ms / 86400000, " days");
        }

        return StrUtils.concatenate(ms, " milliseconds");
    }
}
