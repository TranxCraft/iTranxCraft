package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_commandviewer extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TranxPlayer tSender = null;

        if (sender instanceof Player) {
            Player bukkitPlayer = (Player) sender;
            tSender = plugin.playerManager.getPlayer(sender);

            if (!tSender.hasRank(Rank.MODERATOR)) {
                return noPerms();
            }
        }

        Admin admin = Admin.fromUuid(tSender.getUuid());

        admin.setCommandViewer(!admin.hasCommandViewer());

        sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Command Viewer is now ", (admin.hasCommandViewer() ? "enabled" : "disabled"), "."));

        return true;
    }
}
