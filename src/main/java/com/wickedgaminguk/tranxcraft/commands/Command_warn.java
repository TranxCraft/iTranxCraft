package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_warn extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        TranxPlayer tSender = null;

        if (sender instanceof Player) {
            tSender = plugin.playerManager.getPlayer(playerSender);
        }

        if (args.length == 0 || args.length == 1) {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
            return true;
        }

        if (sender instanceof Player) {
            if (player == playerSender) {
                sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Don't try to warn yourself, idiot."));
                return true;
            }

            if (!tSender.hasRank(Rank.LEADADMIN)) {
                if (tSender.isAdmin()) {
                    sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You may not warn ", player.getName()));
                    return true;
                }
            }
        }

        String message = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        player.sendMessage(StrUtils.concatenate(ChatColor.RED, "[WARNING] ",  message));

        return true;
    }
}
