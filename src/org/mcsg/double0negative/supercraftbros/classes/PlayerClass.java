package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public interface PlayerClass {
    
    Player player = null;
    
    public enum ClassType {
        ENDERMAN, CACTUS, CREEPER, SKELETON, SPIDER, WITHER, ZOMBIE, UNKNOWN, GHAST, ENDERDRAGON, BLAZE, WITCH;
    }
    
    public void PlayerDamaged();
    
    public void PlayerDeath();
    
    public void PlayerAttack(Player victim);
    
    public void PlayerSpawn();
    
    public void PlayerPlaceBlock(Block b);
    
    public ClassType getType();
    
    PlayerClass newInstance(Player p);
    
    public void PlayerShootArrow(Entity pro);
    
    public void PlayerInteract(Action a);
    
    public void Smash();
    
    public void PlayerMove();
    
    public String getName();
    
    public ChatColor getPrefix();
    
}
