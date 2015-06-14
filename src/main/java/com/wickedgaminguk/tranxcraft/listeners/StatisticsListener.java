package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsListener extends Listener<TranxCraft> {

    private HashMap<Player, AtomicInteger> stepCache = new HashMap<>();
    private int totalStepCache = 0;

    //region Player Events

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            incrementPlayerStatistic(event.getEntity(), "deaths");
            plugin.sqlModule.incrementStatistic("global_player_deaths");

            if (event.getEntity().getKiller() instanceof Player) {
                incrementPlayerStatistic(event.getEntity().getKiller(), "kills");
                plugin.sqlModule.incrementStatistic("global_player_kills");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "joins");
        plugin.sqlModule.incrementStatistic("global_player_joins");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "leaves");
        plugin.sqlModule.incrementStatistic("global_player_leaves");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "messages");
        plugin.sqlModule.incrementStatistic("global_player_messages");
    }

    @EventHandler
    public void onPlayerAchievementAward(PlayerAchievementAwardedEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "achievements");
        plugin.sqlModule.incrementStatistic("global_player_achievements");
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "bed_joins");
        plugin.sqlModule.incrementStatistic("global_player_bed_joins");
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "bed_leaves");
        plugin.sqlModule.incrementStatistic("global_player_bed_leaves");
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "bucket_fills");
        plugin.sqlModule.incrementStatistic("global_player_bucket_fills");
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "bucket_empties");
        plugin.sqlModule.incrementStatistic("global_player_bucket_empties");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1).toLowerCase().split(" ")[0];

        incrementPlayerStatistic(event.getPlayer(), "commands");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate("command_", command));

        plugin.sqlModule.incrementStatistic("global_player_commands");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_player_command_", command));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        String item = convertItemName(event.getItemDrop().getItemStack());

        incrementPlayerStatistic(event.getPlayer(), "item_drops");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate(item, "_drops"));
        plugin.sqlModule.incrementStatistic("global_player_item_drops");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_player_", item, "_drops"));
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        String item = convertItemName(event.getItem().getItemStack());

        incrementPlayerStatistic(event.getPlayer(), "item_pickups");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate(item, "_pickups"));
        plugin.sqlModule.incrementStatistic("global_player_item_pickups");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_player_", item, "_pickups"));
    }

    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "egg_throws");
        plugin.sqlModule.incrementStatistic("global_player_egg_throws");
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "fish");
        plugin.sqlModule.incrementStatistic("global_player_fish");
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        String item = event.getBrokenItem().toString();

        incrementPlayerStatistic(event.getPlayer(), "item_breaks");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate(item, "_breaks"));
        plugin.sqlModule.incrementStatistic("global_player_item_breaks");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_player_", item, "_breaks"));
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        String item = event.getItem().getType().toString();

        incrementPlayerStatistic(event.getPlayer(), "items_consumed");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate(item, "consumed"));
        plugin.sqlModule.incrementStatistic("global_player_items_consumed");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_player_", item, "_consumed"));
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        if (event.getNewLevel() > event.getOldLevel()) {
            incrementPlayerStatistic(event.getPlayer(), "level_ups");
            plugin.sqlModule.incrementStatistic("global_player_level_ups");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        totalStepCache++;
        int stepCount = 0;

        if (stepCache.containsKey(event.getPlayer())) {
            stepCount = stepCache.get(event.getPlayer()).incrementAndGet();
        }
        else {
            stepCache.put(event.getPlayer(), new AtomicInteger(0));
        }

        if (stepCache.get(event.getPlayer()).get() >= 500) {
            incrementPlayerStatistic(event.getPlayer(), "steps_taken", stepCount);
            stepCache.get(event.getPlayer()).set(0);
        }

        if (totalStepCache >= 1000) {
            plugin.sqlModule.incrementStatistic("global_player_steps_taken", totalStepCache);
            totalStepCache = 0;
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "portals");
        plugin.sqlModule.incrementStatistic("global_player_portals");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "respawns");
        plugin.sqlModule.incrementStatistic("global_player_respawns");
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        incrementPlayerStatistic(event.getPlayer(), "teleports");
        plugin.sqlModule.incrementStatistic("global_player_teleports");
    }

    //endregion

    //region Entity Events

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        String name = event.getEntityType().toString().replace("_", "").toLowerCase();

        plugin.sqlModule.incrementStatistic("global_entity_spawns");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_entity_", name, "_spawns"));
    }

    @EventHandler
    public void onSuperCreeper(CreeperPowerEvent event) {
        plugin.sqlModule.incrementStatistic("global_entity_creeper_powerups");
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        String firer = event.getEntity().getType().toString().replace("_", "").toLowerCase();

        plugin.sqlModule.incrementStatistic("global_creature_");
    }

    //endregion

    //region Weather Events

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        plugin.sqlModule.incrementStatistic("global_weather_lightning");
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState()) {
            plugin.sqlModule.incrementStatistic("global_weather_thunder");
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_weather_", (event.toWeatherState() ? "rain" : "sun")));
    }

    //endregion

    //region Vehicle Events

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        plugin.sqlModule.incrementStatistic("global_vehicle_creates");
    }

    @EventHandler
    public void onVehicleDestory(VehicleDestroyEvent event) {
        plugin.sqlModule.incrementStatistic("global_vehicle_destroys");
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        plugin.sqlModule.incrementStatistic("global_vehicle_exits");
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        plugin.sqlModule.incrementStatistic("global_vehicle_moves");
    }

    //endregion

    //region Server Events

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        plugin.sqlModule.incrementStatistic("global_server_list_pings");
    }

    //endregion

    //region Hanging Entity Events

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        String block = event.getBlock().toString().toLowerCase();

        incrementPlayerStatistic(event.getPlayer(), "hanging_places");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate("hanging_", block, "_places"));
        plugin.sqlModule.incrementStatistic("global_hanging_places");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_hanging_", block, "_places"));
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            incrementPlayerStatistic(((Player) event.getRemover()).getPlayer(), StrUtils.concatenate("hanging_", event.getEntity().toString(), "_breaks"));
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        String block = event.getEntity().toString();

        plugin.sqlModule.incrementStatistic("global_hanging_breaks");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_hanging_", block, "_breaks"));
    }

    //endregion

    //region Inventory Events

    @EventHandler
    public void onBrew(BrewEvent event) {
        plugin.sqlModule.incrementStatistic("global_inventory_brews");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        String item = event.getInventory().getResult().getType().toString();

        plugin.sqlModule.incrementStatistic("global_inventory_crafts");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_inventory_", item, "_crafts"));
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        String item = event.getBlock().getType().toString().toLowerCase();

        plugin.sqlModule.incrementStatistic("global_inventory_furnace_burns");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_inventory_furnace_", item, "_burns"));
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        String item = event.getBlock().getType().toString().toLowerCase();

        plugin.sqlModule.incrementStatistic("global_inventory_furnace_smelts");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_inventory_furnace_", item, "_smelts"));
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        String item = event.getBlock().getType().toString().toLowerCase();

        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate("inventory_furnace_", item, "_extracts"));
        incrementPlayerStatistic(event.getPlayer(), "inventory_furnace_extracts");
        plugin.sqlModule.incrementStatistic("global_inventory_furnace_extracts");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_inventory_furnace_", item, "_extracts"));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        incrementPlayerStatistic((Player) event.getPlayer(), "inventory_open");
        plugin.sqlModule.incrementStatistic("global_inventory_open");
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        incrementPlayerStatistic((Player) event.getPlayer(), "inventory_close");
        plugin.sqlModule.incrementStatistic("global_inventory_close");
    }

    //endregion

    //region Enchantment Events

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        String item = event.getItem().getType().toString();
        int cost = event.getExpLevelCost();

        incrementPlayerStatistic(event.getEnchanter(), StrUtils.concatenate("enchant_", item, "_enchanted"));
        incrementPlayerStatistic(event.getEnchanter(), "enchant_items_enchanted");
        incrementPlayerStatistic(event.getEnchanter(), "enchant_cost", cost);

        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_enchant_", item, "_enchanted"));
        plugin.sqlModule.incrementStatistic("global_enchant_items_enchanted");
        plugin.sqlModule.incrementStatistic("global_enchant_cost", cost);
    }

    //endregion

    //region Block Events

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String block = event.getBlock().getType().toString().toLowerCase();

        incrementPlayerStatistic(event.getPlayer(), "block_placed");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate("block_", block, "_placed"));

        plugin.sqlModule.incrementStatistic("global_block_placed");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_block_", block, "_placed"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        int experience = event.getExpToDrop();
        String block = event.getBlock().getType().toString().toLowerCase();

        incrementPlayerStatistic(event.getPlayer(), "block_broken");
        incrementPlayerStatistic(event.getPlayer(), StrUtils.concatenate("block_", block, "_broken"));
        incrementPlayerStatistic(event.getPlayer(), "block_experience_gained", experience);

        plugin.sqlModule.incrementStatistic("global_block_broken");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_block_", block, "_broken"));
        plugin.sqlModule.incrementStatistic("global_block_experience_gained", experience);
    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent event) {
        plugin.sqlModule.incrementStatistic("global_block_burned");
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("global_block_", event.getBlock().getType().toString().toLowerCase(), "_burned"));
    }

    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
        plugin.sqlModule.incrementStatistic("global_block_piston_triggered");
        plugin.sqlModule.incrementStatistic("global_block_piston_extend");
    }

    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        plugin.sqlModule.incrementStatistic("global_block_piston_triggered");
        plugin.sqlModule.incrementStatistic("global_block_piston_retract");
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        plugin.sqlModule.incrementStatistic("global_block_sign_change");
    }

    //endregion

    //region Helper Methods

    private void incrementPlayerStatistic(Player player, String statistic) {
        //plugin.sqlModule.incrementStatistic(StrUtils.concatenate("player_", player.getUniqueId().toString(), "_", statistic));
        incrementPlayerStatistic(player, statistic, 1);
    }

    private void incrementPlayerStatistic(Player player, String statistic, int amount) {
        plugin.sqlModule.incrementStatistic(StrUtils.concatenate("player_", player.getUniqueId().toString().replace("-", ""), "_", statistic), amount);
    }

    private String convertItemName(ItemStack itemStack) {
        return itemStack.getType().toString().replace("_", "").toLowerCase();
    }

    //endregion
}
