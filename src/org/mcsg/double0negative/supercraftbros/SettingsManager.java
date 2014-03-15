package org.mcsg.double0negative.supercraftbros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class SettingsManager {
    
    // makes the config easily accessible
    
    private static SettingsManager instance = new SettingsManager();
    private static Plugin p;
    private FileConfiguration spawns;
    private FileConfiguration system;
    
    private File f;
    private File f2;
    
    public static SettingsManager getInstance() {
        return instance;
    }
    
    public void setup(Plugin plug) {
        p = plug;
        p.getConfig().options().copyDefaults(true);
        p.saveDefaultConfig();
        
        f = new File(p.getDataFolder(), "spawns.yml");
        f2 = new File(p.getDataFolder(), "system.yml");
        
        try {
            if (!f.exists()) f.createNewFile();
            if (!f2.exists()) f2.createNewFile();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        reloadSystem();
        saveSystemConfig();
        
        reloadSpawns();
        saveSpawns();
        
    }
    
    public void set(String arg0, Object arg1) {
        p.getConfig().set(arg0, arg1);
    }
    
    public FileConfiguration getConfig() {
        return p.getConfig();
    }
    
    public FileConfiguration getSystemConfig() {
        return system;
    }
    
    public FileConfiguration getSpawns() {
        return spawns;
    }
    
    public void saveConfig() {
        // p.saveConfig();
    }
    
    public static World getGameWorld(int game) {
        if (SettingsManager.getInstance().getSystemConfig().getString("system.arenas." + game + ".world") == null) {
            // LobbyManager.getInstance().error(true);
            return null;
            
        }
        return p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("system.arenas." + game + ".world"));
    }
    
    public void reloadSpawns() {
        spawns = YamlConfiguration.loadConfiguration(f);
    }
    
    public void reloadSystem() {
        system = YamlConfiguration.loadConfiguration(f2);
    }
    
    public void saveSystemConfig() {
        try {
            system.save(f2);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void saveSpawns() {
        try {
            spawns.save(f);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public int getSpawnCount(int gameid) {
        return spawns.getInt("spawns." + gameid + ".count");
    }
    
    public Location getLobbySpawn() {
        return new Location(Bukkit.getWorld(system.getString("system.lobby.spawn.world")), system.getInt("system.lobby.spawn.x"), system.getInt("system.lobby.spawn.y"), system.getInt("system.lobby.spawn.z"));
    }
    
    public Location getGameLobbySpawn(int gameid) {
        return new Location(getGameWorld(gameid), spawns.getInt("spawns." + gameid + ".lobby.x"), spawns.getInt("spawns." + gameid + ".lobby.y"), spawns.getInt("spawns." + gameid + ".lobby.z"));
    }
    
    public void setGameLobbySpawn(int gameid, Location l) {
        spawns.set("spawns." + gameid + ".lobby.world", l.getWorld().getName());
        spawns.set("spawns." + gameid + ".lobby.x", l.getBlockX());
        spawns.set("spawns." + gameid + ".lobby.y", l.getBlockY());
        spawns.set("spawns." + gameid + ".lobby.z", l.getBlockZ());
        saveSpawns();
    }
    
    public Location getSpawnPoint(int gameid, int spawnid) {
        return new Location(getGameWorld(gameid), spawns.getInt("spawns." + gameid + "." + spawnid + ".x"), spawns.getInt("spawns." + gameid + "." + spawnid + ".y"), spawns.getInt("spawns." + gameid + "." + spawnid + ".z"));
    }
    
    public void setLobbySpawn(Location l) {
        system.set("system.lobby.spawn.world", l.getWorld().getName());
        system.set("system.lobby.spawn.x", l.getBlockX());
        system.set("system.lobby.spawn.y", l.getBlockY());
        system.set("system.lobby.spawn.z", l.getBlockZ());
        saveSystemConfig();
    }
    
    public void setSpawn(int gameid, int spawnid, Vector v) {
        spawns.set("spawns." + gameid + "." + spawnid + ".x", v.getBlockX());
        spawns.set("spawns." + gameid + "." + spawnid + ".y", v.getBlockY());
        spawns.set("spawns." + gameid + "." + spawnid + ".z", v.getBlockZ());
        if (spawnid > spawns.getInt("spawns." + gameid + ".count")) {
            spawns.set("spawns." + gameid + ".count", spawnid);
        }
        try {
            spawns.save(f);
        }
        catch (IOException e) {
            
        }
        GameManager.getInstance().getGame(gameid).addSpawn();
        
    }
    
    public static String getSqlPrefix() {
        
        return getInstance().getConfig().getString("sql.prefix");
    }
    
    public void loadFile(String file) {
        File t = new File(p.getDataFolder(), file);
        
        try {
            t.createNewFile();
            FileWriter out = new FileWriter(t);
            InputStream is = getClass().getResourceAsStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line + "\n");
            }
            out.flush();
            is.close();
            isr.close();
            br.close();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
