package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Ban;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.player.TranxPlayer;
import com.wickedgaminguk.tranxcraft.util.PlayerUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_ban extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!PlayerUtils.checkPermissions(sender, Rank.MODERATOR)) {
            return noPerms();
        }

        if (args.length <= 1) {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "That player is either offline, or they do not exist."));
            return true;
        }

        if (sender instanceof Player) {
            if (player == playerSender) {
                sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "Don't try to ban yourself, idiot."));
                return true;
            }
        }

        Admin adminSender = Admin.fromUuid(playerSender.getUniqueId().toString());
        TranxPlayer tranxPlayer = plugin.playerManager.getPlayer(player);
        
        if (adminSender.getRank().getRankLevel() <= tranxPlayer.getRank().getRankLevel()) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED, "You may not ban ", player.getName()));
            return true;
        }

        String banReason = null;

        if (args.length >= 2) {
            banReason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        Bukkit.broadcastMessage(StrUtils.concatenate(ChatColor.RED, sender.getName(), " - banning ", player.getName(), " for ", banReason));
        
        player.setGameMode(GameMode.SURVIVAL);

        player.kickPlayer(StrUtils.concatenate(ChatColor.RED, "You have been banned by ", sender.getName(), ".", (banReason != null ? ("\nReason: " + ChatColor.YELLOW + banReason) : "")));
        
        Ban ban = new Ban();
        ban.setAdmin(sender.getName());
        ban.setIp(player.getAddress().getHostString());
        ban.setPlayer(player.getName());
        ban.setReason(banReason);
        ban.setUuid(player.getUniqueId().toString());
        
        plugin.banManager.addBan(ban);

        return true;
    }
}