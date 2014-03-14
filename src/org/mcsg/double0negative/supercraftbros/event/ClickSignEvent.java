package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class ClickSignEvent implements Listener {
    private Location bLocation;
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)) return;
        Block clickedBlock = e.getClickedBlock();
        if (!(clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.WALL_SIGN)) return;
        Sign thisSign = (Sign) clickedBlock.getState();
        String[] lines = thisSign.getLines();
        if (ChatColor.stripColor(lines[0]).equalsIgnoreCase("[class]")) {
            String cl = lines[1];
            Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer()));
            if (g != null) {
                g.setPlayerClass(e.getPlayer(), GameManager.getInstance().classList.get(cl.toLowerCase()).newInstance(e.getPlayer()));
                g.getPlayerClassBase(e.getPlayer()).PlayerSpawn();
                if (g.getBoard() != null) g.getBoard().setup(true);
            }
        }
        else if (ChatColor.stripColor(lines[0]).equalsIgnoreCase("[SCB]")) {
            try {
                int game = Integer.parseInt(ChatColor.stripColor(lines[1]));
                Game g = GameManager.getInstance().getGame(game);
                g.addPlayer(e.getPlayer());
                g.updateLoadedSigns(e.getPlayer().getWorld(), false);
            }
            catch (Exception exception) {}
        }
        else if (ChatColor.stripColor(lines[0]).equalsIgnoreCase("[leave]")) {
            Player p = e.getPlayer();
            p.performCommand("scb leave");
        }
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[scb]")) {
            bLocation = e.getBlock().getLocation();
            World w = bLocation.getWorld();
            Block b = w.getBlockAt(bLocation);
            int game = Integer.parseInt(e.getLine(1));
            Game g = GameManager.getInstance().getGame(game);
            e.setLine(0, "§3[SCB]");
            e.setLine(2, "§eClick to join");
            e.setLine(3, "§b" + g.getActivePlayers().size() + " /4");
            b.getState().update();
            
        }
        else if (e.getLine(0).equalsIgnoreCase("[class]")) {
            e.setLine(0, "§2[Class]");
            e.setLine(2, "§bClick to pick");
            e.setLine(3, "§bA class");
        }
        else if (e.getLine(0).equalsIgnoreCase("[leave]")) {
            e.setLine(0, "§2[Leave]");
            e.setLine(2, "§bClick to");
            e.setLine(3, "§bleave");
        }
        else if (e.getLine(0).equalsIgnoreCase("[open]")) {
            e.setLine(0, "§1[Open]");
            e.setLine(2, "§aChoose a");
            e.setLine(3, "§bClasss!");
        }
    }
    
    /*
     * @EventHandler public void blockPlaced(SignChangeEvent event) { String[]
     * a= event.getLines(); if (a[0].equals("[join]")) { bLocation =
     * event.getBlock().getLocation(); } } public void signUpdater(Location
     * bLocation) { World w = bLocation.getWorld(); Block b =
     * w.getBlockAt(bLocation); if(b instanceof Sign){ Sign sign = (Sign) b;
     * sign.setLine(2, "Test"); b.getState().update(); } } public boolean
     * onCommand(CommandSender sender, Command cmd, String label, String[]
     * args){ if(cmd.getName().equalsIgnoreCase("signs")) {
     * signUpdater(bLocation); return true; } return false; }
     */
}
