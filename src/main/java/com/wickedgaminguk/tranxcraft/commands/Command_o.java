package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.ChatUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_o extends BukkitCommand<TranxCraft> {

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

        if (args.length == 0) {
            //TODO: Toggleable AdminChat
            return true;
        }

        String message = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");

        ChatUtils.sendAdminChatMessage(tSender.getAdmin(), message);
        
        return true;
    }
}
