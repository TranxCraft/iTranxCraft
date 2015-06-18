package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.WarpUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_warplist extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        String[] warps = WarpUtils.getWarpList();

        if (warps == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "No warps found."));
        }
        else {
            sender.sendMessage(StrUtils.concatenate("Warps:\n", ChatColor.GOLD, StringUtils.join(warps, ", ")));
        }

        return true;
    }
}
