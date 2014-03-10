package org.mcsg.double0negative.supercraftbros.classes;

import java.util.Random;

import org.bukkit.ChatColor;
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

public class SpiderClass extends PlayerClassBase {
    
    public SpiderClass(Player p) {
        super(p);
        
    }
    
    public void PlayerAttack(Player victim) {
        Random r = new Random();
        if (r.nextInt(50) == 25) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30, 2));
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void PlayerSpawn() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8000, 2));
        
        PlayerInventory i = player.getInventory();
        i.clear();
        
        i.setHelmet(new HeadCreator().getPlayerhead("MHF_Spider", getType().getColor() + "Spider Head"));
        i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 0, 0, 0));
        
        ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 0, 0, 0);
        legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        i.setLeggings(legs);
        
        ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS), 0, 0, 0);
        boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
        i.setBoots(boot);
        
        ItemStack i1 = new ItemStack(Material.SPIDER_EYE);
        i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        i1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        i.addItem(i1);
        player.setDisplayName(ChatColor.BLACK + "[Spider]" + ChatColor.WHITE + player.getName());
        
        player.updateInventory();
        
    }
    
    public SpiderClass newInstance(Player p) {
        return new SpiderClass(p);
    }
    
    public String getName() {
        return "Spider";
    }
    
    @Override
    public ClassType getType() {
        // TODO Auto-generated method stub
        return ClassType.SPIDER;
    }
    
    @Override
    public ChatColor getPrefix() {
        return ChatColor.BLACK;
    }
    
    @Override
    public void PlayerDamaged() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void PlayerInteract(Action action) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void PlayerDeath() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void PlayerShootArrow(Entity projectile) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void Smash() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void PlayerPlaceBlock(Block block) {
        // TODO Auto-generated method stub
        
    }
}
