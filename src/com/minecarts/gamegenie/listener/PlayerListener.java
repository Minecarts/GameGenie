package com.minecarts.gamegenie.listener;

import com.minecarts.gamegenie.GameGenie;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
    private GameGenie plugin;
    public PlayerListener(GameGenie plugin){
        this.plugin = plugin;
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent e){
        //Restore their inventory if they quit
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.CREATIVE){
            ItemStack[] inventory = plugin.retrieveInventory(p);
            if(inventory != null){
                System.out.println(plugin.pdf.getName()+ "> Restored player inventory on logout for " + p.getName());
                p.getInventory().setContents(inventory);
            }
        }
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.CREATIVE){
            if(p.hasPermission("gamegenie.allowdrop") || p.isOp()){
                return;
            }
            p.sendMessage(ChatColor.RED+ "ATTENTION: " + ChatColor.WHITE + "You cannot drop items in creative mode.");
            e.setCancelled(true);
        }
    }
}
