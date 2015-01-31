package com.wickedgaminguk.tranxcraft.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.net.InternetDomainName;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.util.IpUtils;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Map;

public class Util {

    public final Map<String, String> HARD_BANS = ImmutableMap.<String, String>builder()
            .put("3d4ad828721f44a4b6e1a18aeac31f88", "xXWilee999Xx, you are a pot stirring fuck, you're not allowed on TranxCraft, ever.")
            .build();

    private TranxCraft plugin;

    public Util(TranxCraft plugin) {
        this.plugin = plugin;
    }
}
