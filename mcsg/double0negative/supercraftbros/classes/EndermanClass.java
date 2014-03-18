package org.mcsg.double0negative.supercraftbros.classes;

import net.minecraft.server.v1_7_R1.PacketPlayOutWorldEvent;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

import com.gmail.Jacob6816.scb.utils.HeadCreator;

public class EndermanClass extends PlayerClassBase {
    
    public EndermanClass(Player p) {
        super(p);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void PlayerSpawn() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80000, 2));
        
        PlayerInventory i = player.getInventory();
        i.clear();
        
        i.setHelmet(new HeadCreator().getPlayerhead("MHF_Enderman", ChatColor.DARK_PURPLE + "Enderman Head"));
        i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 37, 6, 39));
        
        ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 37, 6, 39);
        legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        i.setLeggings(legs);
        
        ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS), 37, 6, 39);
        boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
        i.setBoots(boot);
        
        ItemStack i1 = new ItemStack(381);
        i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        i1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        i.addItem(i1);
        
        i.addItem(new ItemStack(Material.ENDER_PEARL, 20));
        player.setDisplayName(ChatColor.DARK_PURPLE + "[Enderman]" + ChatColor.WHITE + player.getName());
        
        player.updateInventory();
        
    }
    
    @Override
    public EndermanClass newInstance(Player p) {
        return new EndermanClass(p);
    }
    
    @Override
    public String getName() {
        return "Enderman";
    }
    
    public boolean sne = false;
    
    @Override
    public void PlayerMove() {
        super.PlayerMove();
        if (!fsmash) {
            if (smash) {
                if (player.isSneaking()) {
                    sne = true;
                    Location l = player.getLocation();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1000, 1));
                    SendPacketToAll(new PacketPlayOutWorldEvent(2003, l.getBlockX(), l.getBlockY() + 1, l.getBlockZ(), 0, false));
                    
                }
            }
            
        }
    }
    
    @Override
    public void PlayerDamaged() {
        
    }
    
    @Override
    public void PlayerInteract(Action action) {
        
    }
    
    @Override
    public void PlayerAttack(Player victim) {
        
    }
    
    @Override
    public void PlayerDeath() {
        
    }
    
    @Override
    public void PlayerShootArrow(Entity projectile) {
        
    }
    
    @Override
    public void Smash() {
        
    }
    
    @Override
    public void PlayerPlaceBlock(Block block) {
        
    }
    
    @Override
    public ClassType getType() {
        return ClassType.ENDERMAN;
    }
    
}
