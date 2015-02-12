package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LocationUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityListener extends Listener<TranxCraft> {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntityType().equals(EntityType.PRIMED_TNT))) {
            LoggerUtils.info("A " + WordUtils.capitalizeFully(event.getEntityType().toString().toLowerCase()) + " exploded at: " + LocationUtils.format(event.getLocation()));
            event.setCancelled(true);
        }
    }
}