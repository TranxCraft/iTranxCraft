package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.List;

@CommandPermissions(source = SourceType.ANY)
public class Command_reloadcache extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        List<String> arguments = Arrays.asList("players", "admins", "bans", "all");

        if (!PlayerUtils.checkPermissions(sender, Rank.COMMANDER)) {
            return noPerms();
        }

        if (args.length == 0) {
            return false;
        }

        if (!arguments.contains(args[0].toLowerCase())) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid option chosen."));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "admins": {
                Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - Reloading admin cache."));
                plugin.adminManager.reloadCache();

                return true;
            }

            case "players": {
                Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - Reloading player cache."));
                plugin.playerManager.reloadCache();

                return true;
            }

            case "bans": {
                Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - Reloading ban cache."));
                plugin.banManager.reloadCache();

                return true;
            }

            case "all": {
                Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - Reloading all cache."));
                plugin.adminManager.reloadCache();
                plugin.playerManager.reloadCache();
                plugin.banManager.reloadCache();
            }
        }

        return true;
    }
}
