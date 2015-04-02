package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_info extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TranxPlayer tSender = null;

        if (sender instanceof Player) {
            tSender = plugin.playerManager.getPlayer(sender);

            if (!tSender.hasRank(Rank.MODERATOR)) {
                return noPerms();
            }
        }

        if (args.length <= 1) {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
            return true;
        }

        TranxPlayer target = plugin.playerManager.getPlayer(player);

        sender.sendMessage(StrUtils.concatenate("Player info for ", ChatColor.GOLD, target.getName(),
                "\nUUID: ", target.getUuid(),
                "\nName: ", target.getName(),
                "\nForum Name: ", target.getForumName(),
                "\nLatest IP: ", target.getLatestIp(),
                "\nRank: ", WordUtils.capitalizeFully(target.getRank().toString().toLowerCase())
        ));

        return true;
    }
}
