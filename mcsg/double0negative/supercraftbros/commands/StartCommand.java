package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

import com.gmail.Jacob6816.scb.utils.Permissions;

public class StartCommand implements SubCommand {
    
    @Override
    public boolean onCommand(Player player, String[] args) {
        Permissions perms = new Permissions(player);
        if (perms.canForceStart()) {
            int game = GameManager.getInstance().getPlayerGameId(player);
            if (game != -1) {
                GameManager.getInstance().getGame(game).countdown(2);
                player.sendMessage(ChatColor.AQUA + " Arena Started!");
            }
        }
        return true;
    }
    
    @Override
    public String help(Player p) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
