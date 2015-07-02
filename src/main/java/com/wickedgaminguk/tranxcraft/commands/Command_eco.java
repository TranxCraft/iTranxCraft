package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.FetcherUtils;
import com.wickedgaminguk.tranxcraft.util.NumberUtils;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.List;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_eco extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        List<String> arguments = Arrays.asList("give", "set", "reset");

        if (args.length == 0) {
            TranxPlayer senderPlayer = plugin.playerManager.getPlayer(sender);

            sender.sendMessage(StrUtils.concatenate("You currently have a balance of $", senderPlayer.getCurrency()));

            return true;
        }

        if (!arguments.contains(args[0])) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Invalid option chosen."));
            return showUsage();
        }

        switch (args[0]) {
            case "give": {
                if (args.length < 3) {
                    return showUsage();
                }

                if (!NumberUtils.isDouble(args[2])) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You must define a number to use as the amount of currency that you wish to give."));
                    return true;
                }

                double amount = Double.valueOf(args[2]);

                TranxPlayer senderPlayer = plugin.playerManager.getPlayer(sender);

                if (args[1].equalsIgnoreCase("*")) {
                    if (((Bukkit.getOnlinePlayers().size() - 1) * amount) > senderPlayer.getCurrency()) {
                        sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You do not have enough money to give $", args[2], " to everybody online."));
                        return true;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        TranxPlayer tPlayer = plugin.playerManager.getPlayer(player);

                        if (!senderPlayer.getUuid().equals(tPlayer.getUuid())) {
                            tPlayer.addToCurrency(amount);
                            tPlayer.getPlayer().sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have received $", args[2], " from ", sender.getName()));
                        }
                    }

                    sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have successfully given $", args[2], " to everyone online."));
                    return true;
                }

                TranxPlayer player = plugin.playerManager.getPlayer(FetcherUtils.fetchUuid(args[1]).toString());

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "No player found with the name of ", args[1]));
                    return true;
                }

                if (senderPlayer.getCurrency() < amount) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You cannot give more money than you currently have ($", senderPlayer.getCurrency(), ")."));
                    return true;
                }

                senderPlayer.addToCurrency(-amount);
                player.addToCurrency(amount);

                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have successfully given ", player.getName(), " $", args[2], "."));
                player.getPlayer().sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have received $", args[2], " from ", sender.getName()));

                return true;
            }

            case "set": {
                if (!PlayerUtils.checkPermissions(sender, Rank.ADMIN)) {
                    return noPerms();
                }

                if (args.length < 3) {
                    return showUsage();
                }

                if (!NumberUtils.isDouble(args[2])) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You must define a number to use as the amount of currency that you wish to set."));
                    return true;
                }

                TranxPlayer player = plugin.playerManager.getPlayer(FetcherUtils.fetchUuid(args[1]).toString());

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "No player found with the name of ", args[1]));
                    return true;
                }

                player.setCurrency(Double.valueOf(args[2]));

                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have successfully set the balance of ", player.getName(), " to $", Double.valueOf(args[2])));
                player.getPlayer().sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Your balance has been set to $", args[2]));

                return true;
            }

            case "reset": {
                if (!PlayerUtils.checkPermissions(sender, Rank.ADMIN)) {
                    return noPerms();
                }

                if (args.length < 2) {
                    return showUsage();
                }

                TranxPlayer player = plugin.playerManager.getPlayer(FetcherUtils.fetchUuid(args[1]).toString());

                if (player == null) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "No player found with the name of ", args[1]));
                    return true;
                }

                player.setCurrency(0);

                sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have successfully reset the balance of ", player.getName()));
                player.getPlayer().sendMessage(StrUtils.concatenate(ChatColor.RED, "Your balance has been reset."));

                return true;
            }
        }

        return true;
    }
}
