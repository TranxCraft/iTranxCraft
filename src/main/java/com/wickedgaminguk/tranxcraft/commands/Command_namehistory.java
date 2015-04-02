package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.FetcherUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@CommandPermissions(source = SourceType.ANY)
public class Command_namehistory extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length != 1) {
            return showUsage();
        }

        Player player = getPlayer(args[0]);
        UUID playerId;

        if (player != null) {
            playerId = player.getUniqueId();
        }
        else {
            if (ValidationUtils.isValidUuid(args[0])) {
                playerId = UUID.fromString(args[0]);
            }
            else {
                playerId = FetcherUtils.fetchUuid(args[0]);
            }
        }

        if (playerId == null) {
            sender.sendMessage(StrUtils.concatenate(ChatColor.RED + "This player or UUID can not be found."));
        }

        StringBuilder builder = new StringBuilder();

        Map<String, Date> history = FetcherUtils.fetchNameHistory(playerId);

        for (Map.Entry<String, Date> name : history.entrySet()) {
            builder.append(StrUtils.concatenate(ChatColor.GOLD, name.getValue().toString(), ChatColor.WHITE, ": ", ChatColor.AQUA, name.getKey()));
        }

        sender.sendMessage(StrUtils.concatenate(ChatColor.GOLD));

        return true;
    }
}
