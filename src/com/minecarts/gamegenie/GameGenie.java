package com.minecarts.gamegenie;

import java.util.Map;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.block.Action;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import com.minecarts.gamegenie.command.CommandGameGenie;


public class GameGenie extends JavaPlugin implements Listener {
    private final Map<Player, InventoryState> inventories = new HashMap<Player, InventoryState>();
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("gamegenie").setExecutor(new CommandGameGenie(this));
        
        getLogger().info(getDescription().getVersion() + " enabled.");
    }
    
    @Override
    public void onDisable() {
        for(InventoryState state : inventories.values()) {
            if(state.player.hasPermission("gamegenie.bypass.wipe")) continue;
            
            switch(state.gameMode) {
                case SURVIVAL:
                    state.restore();
                    getLogger().info("Inventory restored onDisable() for " + state.player.getName());
                    break;
            }
        }
        getLogger().info("Plugin disabled.");
    }
    

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        
        if(player.hasPermission("gamegenie.bypass.wipe")) return;
        
        InventoryState state = inventories.get(player);
        
        if(state == null || state.gameMode != event.getNewGameMode()) {
            state = new InventoryState(player);
            inventories.put(player, state);
            player.getInventory().clear();
        }
        else {
            inventories.put(player, state.restore());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamegenie.bypass.wipe")) return;
        
        InventoryState state = inventories.get(player);
        if(state == null) return;
        
        switch(state.gameMode) {
            case SURVIVAL:
                state.restore();
                getLogger().info("Inventory restored on player quit for " + state.player.getName());
                break;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamegenie.bypass.drop")) return;
        
        switch(player.getGameMode()) {
            case CREATIVE:
                player.sendMessage(ChatColor.RED + "ATTENTION: " + ChatColor.RESET + "You cannot drop items in creative mode.");
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onPlayerUseEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamegenie.bypass.container")) return;
        
        switch(player.getGameMode()) {
            case CREATIVE:
                if(event.getRightClicked() instanceof InventoryHolder) {
                    player.sendMessage(ChatColor.RED + "ATTENTION: " + ChatColor.RESET + "You cannot use containers in creative mode.");
                    event.setCancelled(true);
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerUseBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("gamegenie.bypass.container")) return;
        
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        switch(player.getGameMode()) {
            case CREATIVE:
                if(event.getClickedBlock() instanceof InventoryHolder) {
                    switch(event.getClickedBlock().getType()) {
                        case WORKBENCH:
                        case ENCHANTMENT_TABLE:
                            player.sendMessage(ChatColor.RED + "ATTENTION: " + ChatColor.RESET + "You cannot use containers in creative mode.");
                            event.setCancelled(true);
                            break;
                    }
                }
                break;
        }
    }
}
