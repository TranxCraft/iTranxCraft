package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_report extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
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
                sender.sendMessage(ChatColor.RED + "Don't try to report yourself, idiot.");
                playerSender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 50));
                return true;
            }
        }

        String report = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        try {
            PlayerUtils.reportPlayer(playerSender, player, report);
            sender.sendMessage(StrUtils.concatenate(ChatColor.GREEN + "Thank you, your report has successfully been logged."));
        }
        catch (Exception ex) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED + "Sorry, your report has failed to be logged. Please try again or report the issue on GitHub."));
        }

        return true;
    }
}
