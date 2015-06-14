package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StaffUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.List;

@CommandPermissions(source = SourceType.ANY)
public class Command_staffmode extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length > 1) {
            return showUsage();
        }

        if (args.length == 0) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.AQUA, "Staff Mode is currently ", (StaffUtils.getStaffMode() ? "enabled." : "disabled.")));
        }

        List<String> modes = Arrays.asList("on", "off", "enable", "disable");
        String mode = args[0].toLowerCase();

        if (!modes.contains(mode)) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid mode selected. Use: ", ChatColor.GOLD, StringUtils.join(modes, ", ")));
            return true;
        }
        else if (mode.equals("enable") || mode.equals("on")) {
            if (!StaffUtils.getStaffMode()) {
                StaffUtils.setStaffMode(sender.getName(), true);
            }
            else {
                sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Staff Mode is already enabled."));
            }
        }
        else {
            if (StaffUtils.getStaffMode()) {
                StaffUtils.setStaffMode(sender.getName(), false);
            }
            else {
                sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Staff Mode is already disabled."));
            }
        }

        return true;
    }
}
