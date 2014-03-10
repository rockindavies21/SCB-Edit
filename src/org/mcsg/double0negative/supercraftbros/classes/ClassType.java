package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.ChatColor;

public enum ClassType {
    ENDERMAN(ChatColor.AQUA), CACTUS(ChatColor.AQUA), CREEPER(ChatColor.AQUA), SKELETON(ChatColor.AQUA), SPIDER(ChatColor.AQUA), WITHER(ChatColor.AQUA), ZOMBIE(ChatColor.AQUA), UNKNOWN(ChatColor.AQUA), GHAST(ChatColor.AQUA), ENDERDRAGON(ChatColor.AQUA), BLAZE(ChatColor.AQUA), WITCH(ChatColor.AQUA);
    
    final ChatColor prefix;
    
    ClassType(ChatColor prefix) {
        this.prefix = prefix;
    }
    
    public ChatColor getColor() {
        return prefix;
    }
    
}
