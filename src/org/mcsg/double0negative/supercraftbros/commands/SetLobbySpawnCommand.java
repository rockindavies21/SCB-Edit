package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

import com.gmail.Jacob6816.scb.utils.Permissions;

public class SetLobbySpawnCommand implements SubCommand {
    
    @Override
    public boolean onCommand(Player player, String[] args) {
        Permissions perms = new Permissions(player);
        if (perms.canSetLobbySpawn()) {
            SettingsManager.getInstance().setLobbySpawn(player.getLocation());
            player.sendMessage(ChatColor.AQUA + "Lobby Created!");
        }
        return true;
    }
    
    @Override
    public String help(Player p) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
