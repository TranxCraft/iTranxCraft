package com.wickedgaminguk.tranxcraft.listeners;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class BlockListener extends Listener<TranxCraft> {
    
    private List<String> blockedItems;

    public BlockListener() {
        blockedItems = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(plugin.sqlModule.getConfigEntry("blockeditems")));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (blockedItems.contains(event.getBlockPlaced().getType()) && plugin.adminManager.isAdmin(player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "The Use of " + WordUtils.capitalizeFully(event.getBlockPlaced().getType().toString()) + " is not permitted on TranxCraft.");
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
            event.setCancelled(true);
        }
    }
}
