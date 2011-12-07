package com.minecarts.gamegenie.command;

import com.minecarts.gamegenie.CommandHandler;
import com.minecarts.gamegenie.GameGenie;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class CommandGameGenie extends CommandHandler {
    public CommandGameGenie(GameGenie plugin){
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("gamegenie.toggle")) return true;
        if(args.length == 0) return false;
        for(String playerName : args){
            List<Player> playerList  = Bukkit.matchPlayer(playerName);
            if(playerList.size() > 1) {
                sender.sendMessage("Too many players matched " + playerName + ". (" + playerList.size() + ")");
                return true;
            } else if(playerList.size() == 0){
                sender.sendMessage("No online players matched " + playerName + ".");
                return true;
            }

            Player p = playerList.get(0);
            if(p.getGameMode() == GameMode.CREATIVE){
                p.getInventory().clear();

                ItemStack[] inventory = plugin.retrieveInventory(p);
                if(inventory != null){
                    p.getInventory().setContents(inventory);
                }
                
                p.setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(ChatColor.GRAY + "Toggled game mode to " + ChatColor.GREEN + "SURVIVAL" + ChatColor.GRAY + " for " + ChatColor.WHITE + p.getDisplayName());
                p.sendMessage(ChatColor.GRAY + sender.getName() + " set your game mode to " + ChatColor.GREEN + "SURVIVAL" + ChatColor.GRAY + ".");
            } else {
                plugin.storeInventory(p,p.getInventory().getContents());
                p.setGameMode(GameMode.CREATIVE);
                p.getInventory().clear();
                sender.sendMessage(ChatColor.GRAY +"Toggled game mode to " + ChatColor.YELLOW + "CREATIVE" + ChatColor.GRAY +" for "+ ChatColor.WHITE + p.getDisplayName());
                p.sendMessage(ChatColor.GRAY + sender.getName() + " set your game mode to " + ChatColor.YELLOW + "CREATIVE" + ChatColor.GRAY + ".");
            }
        }
        return true;
    }
}
