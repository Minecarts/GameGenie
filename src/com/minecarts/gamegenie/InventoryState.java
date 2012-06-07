package com.minecarts.gamegenie;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryState {
    public final Player player;
    public final GameMode gameMode;
    public final ItemStack[] items;
    public final ItemStack[] armor;
    
    public InventoryState(Player player) {
        this.player = player;
        this.gameMode = player.getGameMode();
        this.items = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
    }
    
    public InventoryState restore() {
        InventoryState previous = new InventoryState(player);
        
        player.getInventory().setContents(items);
        player.getInventory().setArmorContents(armor);
        
        return previous;
    }
}
