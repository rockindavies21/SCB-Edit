package org.mcsg.double0negative.supercraftbros.classes;

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

public class SkeletonClass extends PlayerClassBase{

	
	
	public SkeletonClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 2));

		PlayerInventory i = player.getInventory();
		i.clear();
		
		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 250, 250, 250));
		i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 250, 250, 250));
		
		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 250, 250, 250);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);
		i.setLeggings(legs);
		
		
		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  250, 250, 250);
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
		
	public SkeletonClass newInstance(Player p){
		return new SkeletonClass(p);
	}
		
	public String getName(){
		return "Skeleton";
	}

    @Override
    public ClassType getType() {
        // TODO Auto-generated method stub
        return ClassType.SKELETON;
    }


    @Override
    public ChatColor getPrefix() {
        return ChatColor.DARK_GRAY;
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
    public void PlayerAttack(Player victim) {
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
