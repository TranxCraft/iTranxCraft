package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_linkforum extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length != 1) {
            return showUsage();
        }

        TranxPlayer player = plugin.playerManager.getPlayer(playerSender);

        if (!player.getForumName().equals("Not Set")) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You've already linked your forum account to ", ChatColor.GOLD, player.getForumName()));
            return true;
        }

        if (!plugin.ipBoard.isUser(args[0])) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "This forum account does not exist."));
            return true;
        }

        player.setForumName(args[0]);

        return true;
    }
}
