package com.wickedgaminguk.tranxcraft.commands;

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

@CommandPermissions(source = SourceType.ANY)
public class Command_pdelwarp extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length != 1) {
            return showUsage();
        }

        WarpUtils.delPersonalWarp(playerSender.getUniqueId().toString(), args[0]);

        sender.sendMessage(StrUtils.concatenate(ChatColor.AQUA, "Warp ", ChatColor.DARK_AQUA, args[0], ChatColor.AQUA, " has been deleted successfully."));

        return true;
    }
}
