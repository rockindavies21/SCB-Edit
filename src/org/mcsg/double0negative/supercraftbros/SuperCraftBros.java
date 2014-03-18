package org.mcsg.double0negative.supercraftbros;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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

import com.gmail.Jacob6816.scb.utils.ConfigurationReader;
import com.gmail.Jacob6816.scb.utils.InventoryClassEvent;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SuperCraftBros extends JavaPlugin {
    private ConfigurationReader reader = null;
	private YamlConfiguration LANG;
	private File LANG_FILE;
	private Logger log;
    
    @Override
    public void onEnable() {
    	log = Bukkit.getLogger();
    	loadLang();
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
        
        File conf = new File(getDataFolder(), "config.yml");
        if (!conf.exists()) saveDefaultConfig();
        if (getConfig().getBoolean("useConfig")) reader = new ConfigurationReader(this, getConfig());
        
        new BukkitRunnable() {
            @Override
            public void run() {
                GameManager.getInstance().updateAllSigns();
            }
        }.runTaskTimer(this, 20, 20);
    }
    
    @Override
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
    
    public boolean useReader() {
        return reader != null;
    }
    
    public File getLangFile(){
    	return LANG_FILE;
    }
    
    public YamlConfiguration getLang(){
    	return LANG;
    }
    
    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return;
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                log.severe("[SCB] Couldn't create language file.");
                log.severe("[SCB] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        this.LANG = conf;
        this.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            log.log(Level.WARNING, "SCB: Failed to save lang.yml.");
            log.log(Level.WARNING, "SCB: Report this stack trace to <your name>.");
            e.printStackTrace();
        }
    }
}
