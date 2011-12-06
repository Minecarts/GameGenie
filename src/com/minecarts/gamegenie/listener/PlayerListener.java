package com.minecarts.gamegenie.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
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
