package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.WarpUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_warp extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length != 1) {
            return showUsage();
        }

        Location warp = WarpUtils.getGlobalWarp(args[0]);

        if (warp == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "The warp ", ChatColor.DARK_RED, args[0], ChatColor.RED, " can not be found or does not exist."));
            return false;
        }

        playerSender.teleport(warp, TeleportCause.COMMAND);

        return true;
    }
}
