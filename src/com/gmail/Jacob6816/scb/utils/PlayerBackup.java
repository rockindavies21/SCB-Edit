package com.gmail.Jacob6816.scb.utils;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerBackup {
    private Player p;
    private ItemStack[] inventory, armor;
    private double health, maxHealth;
    private Collection<PotionEffect> effects;
    private boolean canFly, isFlying;
    private int food, fire, slot, totalExp;
    private float fall, saturation;
    private Location last;
    
    public PlayerBackup(Player player) {
        p = player;
    }
    
    public void save() {
        effects = p.getActivePotionEffects();
        canFly = p.getAllowFlight();
        isFlying = p.isFlying();
        health = ((Damageable) p).getHealth();
        maxHealth = ((Damageable) p).getMaxHealth();
        food = p.getFoodLevel();
        fall = p.getFallDistance();
        fire = p.getFireTicks();
        last = p.getLocation();
        inventory = p.getInventory().getContents();
        armor = p.getInventory().getArmorContents();
        saturation = p.getSaturation();
        slot = p.getInventory().getHeldItemSlot();
        totalExp = p.getTotalExperience();
    }
    
    public void wipe() {
        if (p.getActivePotionEffects().size() >= 1) for (PotionEffect e : p.getActivePotionEffects()) {
            p.removePotionEffect(e.getType());
        }
        p.setAllowFlight(false);
        p.setMaxHealth(20D);
        p.setHealth(20D);
        p.setFallDistance(0F);
        p.setFireTicks(0);
        p.setFoodLevel(20);
        p.setSaturation(Float.MAX_VALUE);
        p.getInventory().setContents(new ItemStack[] {});
        p.getInventory().setArmorContents(new ItemStack[] {});
        p.setTotalExperience(0);
    }
    
    public void restore() {
        p.addPotionEffects(effects);
        p.setAllowFlight(canFly);
        p.setFlying(isFlying);
        p.setMaxHealth(maxHealth);
        p.setHealth(health);
        p.setFallDistance(fall);
        p.setFireTicks(fire);
        p.setFoodLevel(food);
        p.teleport(last);
        p.getInventory().setContents(inventory);
        p.getInventory().setArmorContents(armor);
        p.setSaturation(saturation);
        p.getInventory().setHeldItemSlot(slot);
        p.setTotalExperience(totalExp);
    }
}
