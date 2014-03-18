package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
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

public class SkeletonClass extends PlayerClassBase {
    
    public SkeletonClass(Player p) {
        super(p);
        
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void PlayerSpawn() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 2));
        
        PlayerInventory i = player.getInventory();
        i.clear();
        
        i.setHelmet(new HeadCreator().getMobhead(SkullType.SKELETON, ChatColor.GRAY + "Skeleton Head"));
        i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 250, 250, 250));
        
        ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 250, 250, 250);
        legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        i.setLeggings(legs);
        
        ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS), 250, 250, 250);
        boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
        i.setBoots(boot);
        
        ItemStack i1 = new ItemStack(Material.BOW);
        i1.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        i1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        i.addItem(i1);
        
        i.addItem(new ItemStack(Material.ARROW, 1));
        player.setDisplayName(ChatColor.GRAY + "[Skeleton]" + ChatColor.WHITE + player.getName());
        
        player.updateInventory();
        
    }
    
    @Override
    public SkeletonClass newInstance(Player p) {
        return new SkeletonClass(p);
    }
    
    @Override
    public String getName() {
        return "Skeleton";
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
        
        return ClassType.SKELETON;
    }
}
