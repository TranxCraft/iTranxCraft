package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_owner extends BukkitCommand {

@Override
    public boolean run(CommandSender sender, Command cmd, String label, String[] args) {
           Player slime = Bukkit.getServer().getPlayer("ImALuckyGuy");
           Player player = (Player) sender;
        if (slime != null)
        {
            player.sendMessage(ChatColor.GREEN + "The owner of TranxCraft, Exbice, is " + ChatColor.DARK_GREEN + "online" + ChatColor.GREEN + "!");
            return true;

        }
        else if (slime == null)
        {
            player.sendMessage(ChatColor.GREEN + "The owner of TranxCraft, Exbice, is " + ChatColor.DARK_RED + "offline" + ChatColor.GREEN + "!");
            return true;
        }

        else
        {

        }
        return true;

    }
    }
