package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

import com.gmail.Jacob6816.scb.utils.Permissions;

public class EnableCommand implements SubCommand{

	@Override
	public boolean onCommand(Player player, String[] args) {
	    Permissions perms = new Permissions(player);
		if(perms.canEnableArena()){
			if(args.length == 1){
				int i = Integer.parseInt(args[0]);
				GameManager.getInstance().getGame(i).enable();
				player.sendMessage(ChatColor.AQUA + "Arena " + i + " Enabled!");
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
