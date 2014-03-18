package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

import com.gmail.Jacob6816.scb.utils.Permissions;

public class CreateArenaCommand implements SubCommand {
    
    @Override
    public boolean onCommand(Player player, String[] args) {
        Permissions perms = new Permissions(player);
        if (perms.canCreateArena()) {
            GameManager.getInstance().createArenaFromSelection(player);
        }
        return true;
    }
    
    @Override
    public String help(Player p) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
