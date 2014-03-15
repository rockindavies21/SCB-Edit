package org.mcsg.double0negative.supercraftbros;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcsg.double0negative.supercraftbros.event.BreakBlock;
import org.mcsg.double0negative.supercraftbros.event.ClickSignEvent;
import org.mcsg.double0negative.supercraftbros.event.InventoryEvents;
import org.mcsg.double0negative.supercraftbros.event.PlayerClassEvents;
import org.mcsg.double0negative.supercraftbros.event.PlayerDamage;
import org.mcsg.double0negative.supercraftbros.event.PlayerLeave;
import org.mcsg.double0negative.supercraftbros.event.PlayerTeleport;
import org.mcstats.Metrics;

import com.gmail.Jacob6816.scb.utils.InventoryClassEvent;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SuperCraftBros extends JavaPlugin {
    
    public void onEnable() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {}
        SettingsManager.getInstance().setup(this);
        GameManager.getInstance().setup(this);
        
        this.getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        this.getServer().getPluginManager().registerEvents(new ClickSignEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerClassEvents(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClassEvent(), this);
        this.getCommand("scb").setExecutor(new CommandHandler(this));
        
        new BukkitRunnable() {
            public void run() {
                GameManager.getInstance().updateAllSigns();
            }
        }.runTaskTimer(this, 20, 20);
    }
    
    public void onDisable() {
        for (Game g : GameManager.getInstance().getGames()) {
            g.disable();
        }
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }
    
    public WorldEditPlugin getWorldEdit() {
        Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin) {
            return (WorldEditPlugin) worldEdit;
        }
        else {
            return null;
        }
    }
}
