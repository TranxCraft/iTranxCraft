package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.FetcherUtils;
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

@CommandPermissions(source = SourceType.ANY)
public class Command_unban extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length <= 1) {
            return false;
        }

        String player = args[1];
        String uuid = FetcherUtils.fetchUuid(player).toString();

        if (!plugin.banManager.isBanned(uuid)) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This player is not banned. You can't unban them."));
        }

        if (sender instanceof Player) {
            if (player == sender.getName()) {
                sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You can't unban yourself, since you're not banned."));
                return true;
            }
        }

        Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - unbanning ", player));

        plugin.banManager.removeBan(uuid);

        sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "You have successfully unbanned ", player));

        return true;
    }
}