package com.minecarts.gamegenie;

import com.minecarts.gamegenie.command.CommandGameGenie;
import com.minecarts.gamegenie.listener.PlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class GameGenie extends JavaPlugin{
    private HashMap<Player, ItemStack[]> playerInventory = new HashMap<Player, ItemStack[]>();
    private PluginDescriptionFile pdf;
    public void storeInventory(Player p, ItemStack[] is){
        playerInventory.put(p,is);
    }

    public ItemStack[] retrieveInventory(Player p){
        return playerInventory.remove(p);
    }
    
    public void onEnable(){
        pdf = this.getDescription();

        //Register our one event and our one listener, wooo
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM,new PlayerListener(), Event.Priority.Low,this);
        
        //Register command
        getCommand("gm").setExecutor(new CommandGameGenie(this));
        
        System.out.println(pdf.getName()+"> " + pdf.getVersion() + " Enabled");
    }

    public void onDisable(){
        System.out.println(pdf.getName()+"> " + pdf.getVersion() + " Enabled");
    }
}
