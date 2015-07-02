package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.modules.AnnouncementModule;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.NumberUtils;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Command_announcement extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        List<String> arguments = Arrays.asList("add", "set", "remove", "info", "list");

        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length == 0) {
            return showUsage();
        }

        if (!arguments.contains(args[0])) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid option chosen."));
            return showUsage();
        }

        switch (args[0]) {
            case "add": {
                if (args.length < 4) {
                    return showUsage();
                }

                if (!NumberUtils.isInt(args[2])) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You need to specify an integer for the interval."));
                    return true;
                }

                String announcement = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");

                AnnouncementModule.addAnnouncement(args[1], announcement, Integer.valueOf(args[1]));
            }

            case "remove": {
                if (args.length == 1) {
                    return showUsage();
                }

                AnnouncementModule.removeAnnouncement(args[1]);
            }

            case "info": {
                if (args.length != 1) {
                    return showUsage();
                }

                String announcements = StrUtils.removeWhitespace(StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " "));

                HashMap<String, Integer> a = AnnouncementModule.getAnnouncements(announcements.split(","));

                if (a == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED + "No announcements found."));
                    return true;
                }

                StringBuilder builder = new StringBuilder();

                builder.append("Announcements:");

                for (HashMap.Entry<String, Integer> announcement : a.entrySet()) {
                    builder.append(StrUtils.concatenate("\n", ChatColor.GOLD, announcement.getKey(), ChatColor.DARK_GRAY, ": ", ChatColor.BLUE, announcement.getValue()));
                }

                sender.sendMessage(builder.toString());

                return true;
            }

            case "list": {
                StringBuilder builder = new StringBuilder();

                builder.append("Announcements:");

                HashMap<String, Integer> announcements = AnnouncementModule.getAnnouncements("*");

                if (announcements != null) {
                    for (HashMap.Entry<String, Integer> announcement : announcements.entrySet()) {
                        builder.append(StrUtils.concatenate("\n", ChatColor.GOLD, announcement.getKey(), ChatColor.DARK_GRAY, ": ", ChatColor.BLUE, announcement.getValue()));
                    }

                    sender.sendMessage(builder.toString());
                }
                else {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED + "No announcements found."));
                }

                return true;
            }
        }

        return true;
    }
}
