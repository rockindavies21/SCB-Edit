package com.gmail.Jacob6816.scb.utils;

import org.bukkit.permissions.Permissible;

public class Permissions {
    Permissible p;
    
    public Permissions(Permissible user) {
        p = user;
    }
    
    // Admin permissions
    
    public boolean isAdmin() {
        return p.hasPermission("SCB.Admin") || p.isOp();
    }
    
    public boolean canDisableArena() {
        return p.hasPermission("SCB.Arenas.Disable") || isAdmin();
    }
    
    public boolean canEnableArena() {
        return p.hasPermission("SCB.Arenas.Enable") || isAdmin();
    }
    
    public boolean canCreateArena() {
        return p.hasPermission("SCB.Arenas.Create") || isAdmin();
    }
    
    // Moderator permissions
    
    public boolean isMod() {
        return p.hasPermission("SCB.Mod") || isAdmin();
    }
    
    public boolean canForceStart() {
        return p.hasPermission("SCB.Forcestart") || isMod();
    }
    
    public boolean canSetArenaSpawn() {
        return p.hasPermission("SCB.Spawns.Arena") || isMod();
    }
    
    public boolean canSetLobbySpawn() {
        return p.hasPermission("SCB.Spawns.Arena") || isMod();
    }
    
    public boolean canSetArenaLobby() {
        return p.hasPermission("SCB.Spawns.Arena") || isMod();
    }
    
    // Player permissions
    
    public boolean isPlayer() {
        return p.hasPermission("SCB.Player") || isMod();
    }
    
    public boolean canJoinGame() {
        return p.hasPermission("SCB.Join") || isPlayer();
    }
    
    @Deprecated
    public boolean canLeaveGame() {
        return p.hasPermission("SCB.Leave") || isPlayer();
    }
    
    public boolean canUseClass(String name) {
        return p.hasPermission("SCB.class." + name) || isPlayer();
    }
}
