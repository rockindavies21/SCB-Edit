package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.ChatColor;

public enum ClassType {
    ENDERMAN(ChatColor.DARK_PURPLE), CACTUS(ChatColor.DARK_GREEN), CREEPER(ChatColor.GREEN), SKELETON(ChatColor.DARK_GRAY), SPIDER(ChatColor.BLACK), WITHER(ChatColor.LIGHT_PURPLE), ZOMBIE(ChatColor.DARK_AQUA), UNKNOWN(ChatColor.AQUA), GHAST(ChatColor.DARK_GRAY), ENDERDRAGON(ChatColor.DARK_BLUE), BLAZE(ChatColor.GOLD), WITCH(ChatColor.RED);
    
    final ChatColor prefix;
    
    ClassType(ChatColor prefix) {
        this.prefix = prefix;
    }
    
    public ChatColor getColor() {
        return prefix;
    }
    
}
