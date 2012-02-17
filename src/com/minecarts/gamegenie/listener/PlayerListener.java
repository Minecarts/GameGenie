package com.minecarts.gamegenie.listener;

import com.minecarts.gamegenie.GameGenie;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    private GameGenie plugin;
    public PlayerListener(GameGenie plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e){
        Player p = e.getPlayer();
        switch(e.getNewGameMode()){
            case SURVIVAL:
                ItemStack[] inventory = plugin.retrieveInventory(p);
                if(inventory != null){
                    if(!(p.hasPermission("gamegenie.bypass.wipe"))){
                        p.getInventory().clear();
                        p.getInventory().setContents(inventory);
                    }

                }
                break;
            case CREATIVE:
                if(!(p.hasPermission("gamegenie.bypass.wipe"))){
                    plugin.storeInventory(p,p.getInventory().getContents());
                    p.getInventory().clear();
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        //Restore their inventory if they quit
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.CREATIVE){
            ItemStack[] inventory = plugin.retrieveInventory(p);
            if(inventory != null){
                System.out.println(plugin.pdf.getName()+ "> Restored player inventory on logout for " + p.getName());
                if(!(p.hasPermission("gamegenie.bypass.wipe"))){
                    p.getInventory().setContents(inventory);
                }
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.CREATIVE){
            if(p.hasPermission("gamegenie.bypass.drop") || p.isOp()){
                return;
            }
            p.sendMessage(ChatColor.RED+ "ATTENTION: " + ChatColor.WHITE + "You cannot drop items in creative mode.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityUse(PlayerInteractEntityEvent e){
        if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) return;
        if(e.getRightClicked() == null) return;
        if(e.getRightClicked() instanceof StorageMinecart){
            if(e.getPlayer().hasPermission("gamegenie.bypass.container")){
                return;
            }
            e.getPlayer().sendMessage(ChatColor.RED+ "ATTENTION: " + ChatColor.WHITE + "You cannot use containers in creative mode.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUseContainer(PlayerInteractEvent e){
        if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) return;

        Block clickedBlock = e.getClickedBlock();
        if(clickedBlock == null) return;
        if(clickedBlock.getState() instanceof ContainerBlock
                || clickedBlock.getType().equals(Material.WORKBENCH)
                || clickedBlock.getType().equals(Material.ENCHANTMENT_TABLE)
        ){
            if(e.getPlayer().hasPermission("gamegenie.bypass.container")){
                return;
            }
            e.getPlayer().sendMessage(ChatColor.RED+ "ATTENTION: " + ChatColor.WHITE + "You cannot use containers in creative mode.");
            e.setCancelled(true);
        }
    }
}
