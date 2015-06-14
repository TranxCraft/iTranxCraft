package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.ChatUtils;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_o extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length == 0) {
            if (plugin.adminManager.getToggledAdminChat().contains(playerSender)) {
                plugin.adminManager.getToggledAdminChat().remove(playerSender);
            }
            else {
                plugin.adminManager.getToggledAdminChat().add(playerSender);
            }

            return true;
        }

        String message = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");

        ChatUtils.sendAdminChatMessage(sender.getName(), message);
        
        return true;
    }
}
