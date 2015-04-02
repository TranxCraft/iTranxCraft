package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_setloginmessage extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TranxPlayer tSender = null;

        if (sender instanceof Player) {
            tSender = plugin.playerManager.getPlayer(playerSender);

            if (!tSender.hasRank(Rank.MODERATOR)) {
                return noPerms();
            }
        }

        if (args.length == 0 || args.length == 1) {
            return false;
        }

        String message = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");

        Admin admin = tSender.getAdmin();

        admin.setLoginMessage(message);
        sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN, "Set login message to: ", ChatUtils.colorize(admin.getLoginMessage())));

        return true;
    }
}
