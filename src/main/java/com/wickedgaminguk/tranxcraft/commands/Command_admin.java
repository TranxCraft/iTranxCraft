package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.AdminManager;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.List;

@CommandPermissions(source = SourceType.ANY)
public class Command_admin extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        List<String> arguments = Arrays.asList("add", "set", "remove", "info", "list");

        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }
        
        if (args.length == 0) {
            return false;
        }
        
        if (!arguments.contains(args[0])) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid option chosen."));
            return false;
        }

        Player player = null;

        if (!args[0].equals("list")) {
            player = PlayerUtils.getPlayer(args[1]);
        }

        switch (args[0]) {
            case "add": {
                if (args.length != 3) {
                    return false;
                }

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
                    return true;
                }

                if (AdminManager.isAdmin(player.getUniqueId().toString())) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This player is already an admin."));
                }

                String rank = args[2].toUpperCase();

                if (!ValidationUtils.isInEnum(rank, Rank.class)) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid rank chosen."));
                    return true;
                }

                if (sender instanceof Player) {
                    Admin adminSender = Admin.fromUuid(playerSender.getUniqueId().toString());

                    if (adminSender.getRank().getRankLevel() <= Rank.valueOf(rank).getRankLevel()) {
                        sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You can not add a new admin with a rank equal to or higher than yourself."));
                    }
                }

                if (AdminManager.isAdmin(player.getUniqueId().toString())) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This player is already an admin, please use /admin set instead."));
                }

                plugin.adminManager.addAdmin(player.getUniqueId().toString(), player.getName(), player.getAddress().getHostString(), Rank.valueOf(rank));
                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Added admin ", player.getName(), " successfully."));

                break;
            }
            
            case "set": {
                if (args.length != 3) {
                    return false;
                }

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
                    return true;
                }

                if (!AdminManager.isAdmin(player.getUniqueId().toString())) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED + "This player is not an admin."));
                }

                String rank = args[2].toUpperCase();

                if (!ValidationUtils.isInEnum(rank, Rank.class)) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid rank chosen."));
                    return true;
                }

                Admin adminSender = Admin.fromUuid(playerSender.getUniqueId().toString());

                if (adminSender.getRank().getRankLevel() <= Rank.valueOf(rank).getRankLevel()) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You can not set the rank of an admin to or higher than yourself."));
                }
                
                plugin.adminManager.setAdmin(player.getUniqueId().toString(), Rank.valueOf(rank));
                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Added admin ", player.getName(), " successfully."));

                break;
            }
            
            case "remove": {
                if (args.length != 2) {
                    return false;
                }

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
                    return true;
                }

                if (!AdminManager.isAdmin(player.getUniqueId().toString())) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This player is not an admin."));
                }

                Admin adminSender = Admin.fromUuid(playerSender.getUniqueId().toString());
                Admin admin = Admin.fromUuid(player.getUniqueId().toString());

                if (adminSender.getRank().getRankLevel() <= admin.getRank().getRankLevel()) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You can not remove an admin with a rank equal to or higher than yourself."));
                }

                plugin.adminManager.removeAdmin(player.getUniqueId().toString());
                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Removed admin ", player.getName(),  " successfully."));

                break;
            }
            
            case "info": {
                if (args.length != 2) {
                    return false;
                }

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
                    return true;
                }

                if (!AdminManager.isAdmin(player.getUniqueId().toString())) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This player is not an admin."));
                    return true;
                }
                
                Admin admin = Admin.fromUuid(player.getUniqueId().toString());
                
                sender.sendMessage(StrUtils.concatenate("Admin info for ", ChatColor.GOLD, admin.getPlayerName(),
                        "\nUUID: ", admin.getUuid(),
                        "\nName: ", admin.getPlayerName(),
                        "\nIP: ", admin.getIp(),
                        "\nRank: ", WordUtils.capitalize(admin.getRank().toString().toLowerCase()),
                        "\nE-mail: ", admin.getEmail()
                ));

                break;
            }
            
            case "list": {
                sender.sendMessage(StrUtils.concatenate("Admins:\n", ChatColor.GOLD, StringUtils.join(plugin.adminManager.getAdmins(), ", ")));

                break;
            }
        }
        
        return true;
    }
}
