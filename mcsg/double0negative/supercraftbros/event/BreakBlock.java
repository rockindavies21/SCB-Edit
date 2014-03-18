package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class BreakBlock implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(BlockBreakEvent e){
    	int game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    	if(game != -1){
    		e.setCancelled(true);
    		
    	}
    	
    	
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(BlockPlaceEvent e){
    	int game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    	if(game != -1){
    		e.setCancelled(true);
    		
    	}
    	
    	
    	
    	
    }
}
