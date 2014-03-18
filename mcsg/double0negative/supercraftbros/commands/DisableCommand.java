package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;

import com.gmail.Jacob6816.scb.utils.Permissions;

public class DisableCommand implements SubCommand {
    
    @Override
    public boolean onCommand(Player player, String[] args) {
        Permissions perms = new Permissions(player);
        if (perms.canDisableArena()) {
            if (args.length == 1) {
                int i = Integer.parseInt(args[0]);
                GameManager.getInstance().getGame(i).disable();
                player.sendMessage(ChatColor.AQUA + "Arena " + i + " Disabled!");
            }
            else if (args.length == 0) {
                for (Game g : GameManager.getInstance().getGames()) {
                    g.disable();
                }
            }
        }
        return true;
    }
    
    @Override
    public String help(Player p) {
        return null;
    }
    
}
