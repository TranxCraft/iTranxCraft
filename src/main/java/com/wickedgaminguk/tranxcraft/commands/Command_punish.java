package com.wickedgaminguk.tranxcraft.commands;

import net.pravian.bukkitlib.command.BukkitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_punish extends BukkitCommand {

  @Override
  public boolean run(final CommandSender sender, Command cmd, String label, String[] args)
  {
    if (! (sender.hasPermission("Whatever you want the perm to be.")))
    {
      sender.sendMessage("§4[iTranxCraft] §cYou don't have enough permissions to execute this command.");
      return true;
    }
    
    int length = args.length;
    
    if (length == 0)
    {
      sender.sendMessage("/punish <player>");
      return true;
    }
    
    if (length == 1)
    {
      boolean playerFound = false;
      
      for (Player player : Bukkit.getServer().getOnlinePlayers())
      {
        if (player.getName().equalsIgnoreCase(args[0]))
        {
          Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - Punishing " + player.getName());
          Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been punished for being a bad player!");
          
          for (int i = 0; i <= 200; i++)
          {
            player.getWorld().strikeLightning(player.getLocation());
          }
          
          player.setHealth(0.0);
          player.setFireTicks(10000);
          sender.sendMessage(ChatColor.GRAY + "You have punished " + player.getName() + ".");
          player.sendMessage(ChatColor.DARK_RED + "You were punished by " + sender.getName() + "." + ChatColor.AQUA + " Next time don't break the rules!");
          playerFound = true;
          break;
        }
      }
      
      if (playerFound == false)
      {
        sender.sendMessage(ChatColor.GRAY + args[0] + " not found!");
      }
      
      return true;
    }
    
    
    return false;
  }
}
