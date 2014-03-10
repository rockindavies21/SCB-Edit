package org.mcsg.double0negative.supercraftbros.classes;

import net.minecraft.server.v1_7_R1.Packet;
import net.minecraft.server.v1_7_R1.PacketPlayOutWorldEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.GameManager;

public abstract class PlayerClassBase {
    
    Player player;
    protected boolean smash = false;
    
    private boolean doublej = false;
    protected boolean fsmash = false;
    
    public PlayerClassBase(Player p) {
        this.player = p;
    }
    
    public abstract PlayerClassBase newInstance(Player p);
    
    public abstract String getName();
    
    public abstract ClassType getType();
    
    public void PlayerMove() {
        if (player.isFlying()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            Vector v = player.getLocation().getDirection().multiply(.5);
            v.setY(.75);
            player.setVelocity(v);
            doublej = true;
        }
        if (isOnGround()) {
            player.setAllowFlight(true);
            if (fsmash) {
                exploadPlayers();
                fsmash = false;
            }
            doublej = false;
            
        }
        if (doublej && player.isSneaking()) {
            player.setVelocity(new Vector(0, -1, 0));
            fsmash = true;
        }
        
    }
    
    @SuppressWarnings("deprecation")
    public boolean isOnGround() {
        Location l = player.getLocation();
        l = l.add(0, -1, 0);
        return l.getBlock().getState().getTypeId() != 0;
    }
    
    @SuppressWarnings("deprecation")
    public void exploadPlayers() {
        int i = GameManager.getInstance().getPlayerGameId(player);
        if (i != -1) {
            Location l = player.getLocation();
            l = l.add(0, -1, 0);
            for (int x = l.getBlockX() - 1; x <= l.getBlockX() + 1; x++) {
                for (int z = l.getBlockZ() - 1; z <= l.getBlockZ() + 1; z++) {
                    SendPacketToAll(new PacketPlayOutWorldEvent(2001, x, l.getBlockY() + 1, z, l.getBlock().getState().getTypeId(), false));
                }
            }
            for (Entity pl : player.getWorld().getEntities()) {
                if (pl != player) {
                    Location l2 = pl.getLocation();
                    double d = pl.getLocation().distance(player.getLocation());
                    if (d < 5) {
                        d = d / 5;
                        pl.setVelocity(new Vector((1.5 - d) * getSide(l2.getBlockX(), l.getBlockX()), 1.5 - d, (1.5 - d) * getSide(l2.getBlockZ(), l.getBlockZ())));
                        
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    public void exploadBlocks(Location l) {
        Location l2 = player.getLocation();
        if (l.getBlock().getState().getTypeId() != 0) {
            final Entity e = l.getWorld().spawnFallingBlock(l, l.getBlock().getState().getTypeId(), l.getBlock().getState().getData().getData());
            e.setVelocity(new Vector((getSide(l.getBlockX(), l2.getBlockX()) * 0.3), .1, (getSide(l.getBlockZ(), l2.getBlockZ()) * 0.3)));
            Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable() {
                public void run() {
                    e.remove();
                }
            }, 5);
        }
    }
    
    public void SendPacketToAll(Packet p) {
        for (Player pl : GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).getActivePlayers()) {
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(p);
        }
    }
    
    public int getSide(int i, int u) {
        if (i > u) return 1;
        if (i < u) return -1;
        return 0;
    }
    
    public abstract void PlayerSpawn();
    
    public abstract void PlayerDamaged();
    
    public abstract void PlayerInteract(Action action);
    
    public abstract void PlayerAttack(Player victim);
    
    public abstract void PlayerDeath();
    
    public abstract void PlayerShootArrow(Entity projectile);
    
    public abstract void Smash();
    
    public abstract void PlayerPlaceBlock(Block block);
}
