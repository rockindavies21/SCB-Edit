package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class InventoryClassEvent implements Listener {
	
	private Inventory inv;
	
	public  InventoryClassEvent() {
        inv = Bukkit.createInventory(null, 9, "Scb Classes!");
                inv.setItem(0, new ItemStack(Material.DIRT, 1));
                inv.setItem(2, new ItemStack(Material.GOLD_BLOCK, 1));
                inv.setItem(5, new ItemStack(Material.GOLD_BLOCK, 1));
        }
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        ItemStack clicked = event.getCurrentItem(); 
        Inventory inventory = event.getInventory(); 
        if (inventory.getName().equals(inv.getName())) { 
        if (clicked.getType() == Material.DIRT) {
        event.setCancelled(true); 
        player.closeInventory(); 
        Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player.getPlayer()));
        if (g != null) {
        	String cl = args.length > 1;
            g.setPlayerClass(player.getPlayer(), GameManager.getInstance().classList.get(cl.toLowerCase()).newInstance(player.getPlayer()));
            g.getPlayerClassBase(player.getPlayer()).PlayerSpawn();
            if (g.getBoard() != null) g.getBoard().setup(true);
        }
        }
        }
        }
	
    @EventHandler
    public void clickHandler(PlayerInteractEvent e) {
        
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)) return;
        
        Block clickedBlock = e.getClickedBlock();
        if (!(clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.WALL_SIGN)) return;
        Sign thisSign = (Sign) clickedBlock.getState();
        String[] lines = thisSign.getLines();
        if (ChatColor.stripColor(lines[0]).equalsIgnoreCase("[open]")) {
            e.getPlayer().openInventory(inv);
            }
	
    }
}
}
