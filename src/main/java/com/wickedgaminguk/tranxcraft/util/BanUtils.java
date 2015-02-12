package com.wickedgaminguk.tranxcraft.util;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class BanUtils extends Util {
    
    private static final Map<String, String> HARD_BANS = ImmutableMap.<String, String>builder()
            .put("3d4ad828721f44a4b6e1a18aeac31f88", "xXWilee999Xx, you are a pot stirring fuck, you're not allowed on TranxCraft, ever.")
            .build();
    
    public static final Map<String, String> getHardBans() {
        return HARD_BANS;
    }
}
