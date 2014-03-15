package org.mcsg.double0negative.supercraftbros.classes;

import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class EnderdragonClass extends PlayerClassBase {
    
    public EnderdragonClass(Player p) {
        super(p);
    }
    
    int fly = 0;
    boolean canFly = false;
    
    public EnderdragonClass newInstance(Player p) {
        return new EnderdragonClass(p);
    }
    
    public String getName() {
        return "Ender Dragon";
    }
    
    public void PlayerMove() {
        super.PlayerMove();
        if (fsmash) { return; }
        if (player.isSneaking()) {
            if (fly < 20 && canFly) {
                player.setVelocity(player.getLocation().getDirection().multiply(2));
                Random r = new Random();
                double h = ((Damageable) player).getHealth();
                player.setHealth(((Damageable) player).getMaxHealth());
                player.getWorld().createExplosion(player.getLocation().add(r.nextInt(6) - 3, r.nextInt(6) - 3, r.nextInt(6) - 3), 2);
                player.setHealth(h);
                fly++;
            }
            else {
                canFly = false;
            }
            
        }
        else if (fly > -30 && !canFly) {
            fly--;
        }
        if (fly == -30) {
            canFly = true;
            fly = 0;
        }
    }
    
    int stid = 0;
    
    public void Smash() {
        smash = true;
        final long time = new Date().getTime();
        stid = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameManager.getInstance().getPlugin(), new Runnable() {
            public void run() {
                Random r = new Random();
                player.setVelocity(player.getLocation().getDirection().multiply(1.2));
                player.getWorld().createExplosion(player.getLocation().add(r.nextInt(6) - 3, r.nextInt(6) - 3, r.nextInt(6) - 3), 2);
                if (new Date().getTime() > time + 5000) {
                    Bukkit.getScheduler().cancelTask(stid);
                    smash = false;
                    
                }
            }
        }, 0, 1);
        
    }
    
    public ClassType getType() {
        return ClassType.ENDERDRAGON;
    }
    
    public void PlayerSpawn() {
        player.getInventory().setContents(new ItemStack[] {});
        player.getInventory().setArmorContents(new ItemStack[] { null, null, new ItemStack(Material.LEATHER_CHESTPLATE), null });
        player.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
        player.getInventory().getItem(0).addEnchantment(Enchantment.DAMAGE_ALL, 3);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4), true);
        player.setHealth(((Damageable) player).getMaxHealth());
    }
    
    public ChatColor getPrefix() {
        return ChatColor.DARK_BLUE;
    }
    
    public void PlayerDamaged() {}
    
    public void PlayerInteract(Action action) {}
    
    public void PlayerAttack(Player victim) {}
    
    public void PlayerDeath() {}
    
    public void PlayerShootArrow(Entity projectile) {}
    
    @SuppressWarnings("deprecation")
    public void PlayerPlaceBlock(Block block) {
        for (BlockFace b : BlockFace.values()) {
            block.getRelative(b).setTypeIdAndData(block.getTypeId(), block.getData(), false);
        }
    }
}
