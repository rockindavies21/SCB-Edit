package com.gmail.Jacob6816.scb.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.SuperCraftBros;

public class ConfigurationReader {
    private FileConfiguration config;
    private SuperCraftBros plugin;
    
    public ConfigurationReader(SuperCraftBros scb, FileConfiguration conf) {
        config = conf;
        plugin = scb;
        
        load();
    }
    
    private void load() {
        GameManager manager = GameManager.getInstance();
        manager.setMaxLives(config.getInt("Integers.Lives"));
        manager.setMaxPlayers(config.getInt("Integers.maxPlayers"));
        manager.setStartCount(config.getInt("Integers.minPlayers"));
    }
    
    public SuperCraftBros getMainClass() {
        return plugin;
    }
}
