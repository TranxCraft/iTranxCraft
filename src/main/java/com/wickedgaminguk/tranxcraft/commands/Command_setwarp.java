package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.WarpUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_setwarp extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length != 1) {
            return showUsage();
        }

        WarpUtils.addWarp(args[0], playerSender.getLocation());

        sender.sendMessage(StrUtils.concatenate(ChatColor.AQUA, "Warp ", ChatColor.DARK_AQUA, args[0], ChatColor.AQUA, " has been created successfully."));

        return true;
    }
}
