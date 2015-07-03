package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.DateUtils;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.text.DateFormat;

@CommandPermissions(source = SourceType.ANY)
public class Command_info extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length != 1) {
            return false;
        }

        Player player = PlayerUtils.getPlayer(args[0]);

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
                "\nRank: ", WordUtils.capitalize(target.getRank().toString().toLowerCase()),
                "\nPlay Time: ", DateUtils.parseMs(target.getPlayTime())
        ));

        return true;
    }
}
