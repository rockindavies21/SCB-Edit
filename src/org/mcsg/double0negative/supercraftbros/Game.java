package org.mcsg.double0negative.supercraftbros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.classes.PlayerClassBase;

import com.gmail.Jacob6816.scb.utils.Gameboard;

public class Game {
    
    public enum State {
        INGAME, LOBBY, DISABLED, WAITING
    }
    
    private int gameID;
    private int spawnCount;
    private Arena arena;
    private State state;
    private Gameboard b;
    private HashMap<Player, Integer> players = new HashMap<Player, Integer>();
    private HashMap<Player, PlayerClassBase> pClasses = new HashMap<Player, PlayerClassBase>();
    private ArrayList<Player> inactive = new ArrayList<Player>();
    private ArrayList<Player> queue = new ArrayList<Player>();
    
    // / List<Location>signLocs = new ArrayList<Location>();
    
    public Game(int a) {
        this.gameID = a;
        
        init();
    }
    
    public void init() {
        FileConfiguration s = SettingsManager.getInstance().getSystemConfig();
        int x = s.getInt("system.arenas." + gameID + ".x1");
        int y = s.getInt("system.arenas." + gameID + ".y1");
        int z = s.getInt("system.arenas." + gameID + ".z1");
        int x1 = s.getInt("system.arenas." + gameID + ".x2");
        int y1 = s.getInt("system.arenas." + gameID + ".y2");
        int z1 = s.getInt("system.arenas." + gameID + ".z2");
        Location max = new Location(SettingsManager.getGameWorld(gameID), Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));
        Location min = new Location(SettingsManager.getGameWorld(gameID), Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));
        arena = new Arena(min, max);
        
        state = State.LOBBY;
        
        spawnCount = SettingsManager.getInstance().getSpawnCount(gameID);
        
    }
    
    public void updateLoadedSigns(World start, boolean loopWorlds) {
        if (!loopWorlds) {
            updateSigns(start);
            return;
        }
        for (World w : Bukkit.getWorlds())
            if (w.getLoadedChunks().length >= 1) updateSigns(w);
    }
    
    private void updateSigns(World world) {
        for (Chunk c : world.getLoadedChunks()) {
            for (BlockState b : c.getTileEntities()) {
                if (!(b instanceof Sign)) continue;
                final Sign s = (Sign) b;
                if (signIsForGame(s)) updateSign(s);
            }
        }
    }
    
    private boolean signIsForGame(Sign s) {
        String[] l = s.getLines();
        if (l[0] == null) return false;
        if (l[1] == null) return false;
        if (!ChatColor.stripColor(l[0]).equalsIgnoreCase("[Join]")) return false;
        try {
            Integer i = Integer.parseInt(l[1]);
            return i == getID();
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    private void updateSign(Sign s) {
        s.setLine(0, ChatColor.DARK_GREEN + "[Join]");
        s.setLine(1, ChatColor.AQUA + "" + getID());
        s.setLine(2, ChatColor.GREEN + localeCaps(state.toString()));
        s.setLine(3, ChatColor.BLUE + "" + getActivePlayers().size() + " / " + 10);
        s.update(true, true);
    }
    
    private String localeCaps(String in) {
        if (in.length() <= 1) return in.toUpperCase();
        String t = in.toLowerCase();
        char c = t.charAt(0);
        String s = String.valueOf(c).toUpperCase();
        return s + t.substring(1);
        
    }
    
    public void addPlayer(Player p) {
        if (state == State.LOBBY && getPlayers().size() < 10) {
            p.teleport(SettingsManager.getInstance().getGameLobbySpawn(gameID));
            
            getPlayers().put(p, 3);
            p.setGameMode(GameMode.SURVIVAL);
            p.setHealth(20D);
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Joined arena " + gameID + ". Select a class! \nHave fun!");
            msgAll(ChatColor.GREEN + p.getName() + " joined the game!" + ChatColor.AQUA + " [+]");
            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
            updateLoadedSigns(p.getWorld(), false);
        }
        else if (state == State.INGAME) {
            p.sendMessage(ChatColor.RED + "Game already started!");
        }
        else if (getPlayers().size() >= 10) {
            p.sendMessage(ChatColor.RED + "Game Full!");
        }
        else {
            p.sendMessage(ChatColor.RED + "Cannot join game!");
        }
        
    }
    
    public void startGame() {
        if (getPlayers().size() < 2) {
            msgAll("Not enough players");
            return;
        }
        inactive.clear();
        state = State.INGAME;
        b = new Gameboard(this);
        b.setup(true);
        for (Player p : getPlayers().keySet().toArray(new Player[0])) {
            if (pClasses.containsKey(p)) {
                spawnPlayer(p);
            }
            else {
                removePlayer(p);
                p.sendMessage(ChatColor.RED + "You didn't pick a class!");
            }
            
        }
        updateLoadedSigns(Bukkit.getWorlds().get(0), true);
    }
    
    int count = 20;
    int tid = 0;
    
    public void countdown(int time) {
        count = time;
        Bukkit.getScheduler().cancelTask(tid);
        
        if (state == State.LOBBY) {
            tid = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) GameManager.getInstance().getPlugin(), new Runnable() {
                public void run() {
                    if (count > 0) {
                        if (count % 10 == 0) {
                            msgAll(ChatColor.BLUE + "Game starting in " + count);
                        }
                        if (count < 6) {
                            msgAll(ChatColor.BLUE + "Game starting in " + count);
                        }
                        count--;
                    }
                    else {
                        startGame();
                        Bukkit.getScheduler().cancelTask(tid);
                    }
                }
            }, 0, 20);
            
        }
    }
    
    boolean started = false;
    
    public void setPlayerClass(Player player, PlayerClassBase playerClass) {
        if (player.hasPermission("scb.class." + playerClass.getName())) {
            clearPotions(player);
            player.sendMessage(ChatColor.GREEN + "You choose " + playerClass.getName() + "!");
            pClasses.put(player, playerClass);
            if (!started && pClasses.keySet().size() >= 4 && getPlayers().size() >= 4) {
                countdown(60);
                started = true;
            }
        }
        else {
            player.sendMessage(ChatColor.RED + "You do not have permission for this class!");
        }
    }
    
    public void killPlayer(Player p, String msg) {
        clearPotions(p);
        
        msgAll(ChatColor.GOLD + msg);
        int lives = getPlayers().get(p) - 1;
        if (lives <= 0) {
            playerEliminate(p);
            b.setup(true);
        }
        else {
            getPlayers().put(p, lives);
            msgAll(p.getName() + " has " + lives + " lives left");
        }
        b.setup(false);
    }
    
    @SuppressWarnings("deprecation")
    public void playerEliminate(Player p) {
        started = false;
        msgAll(ChatColor.DARK_RED + p.getName() + " has been eliminated!");
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        getPlayers().remove(p);
        inactive.add(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[4]);
        p.updateInventory();
        p.setAllowFlight(false);
        p.setFlying(false);
        clearPotions(p);
        p.teleport(SettingsManager.getInstance().getLobbySpawn());
        p.setDisplayName(p.getName());
        if (started) {
            b.hidePlayer(p);
            b.setup(true);
        }
        if (getPlayers().keySet().size() <= 1 && state == State.INGAME) {
            Player pl = null;
            for (Player pl2 : getPlayers().keySet()) {
                pl = pl2;
            }
            Bukkit.broadcastMessage(ChatColor.BLUE + pl.getName() + " won Super Craft Bros on arena " + gameID);
            gameEnd();
        }
        p.setDisplayName(p.getName());
        p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
        updateLoadedSigns(p.getWorld(), false);
    }
    
    public void clearPotions(Player p) {
        for (PotionEffectType e : PotionEffectType.values()) {
            if (e != null && p.hasPotionEffect(e)) p.removePotionEffect(e);
        }
    }
    
    @SuppressWarnings("deprecation")
    public void gameEnd() {
        /*
         * for(Entity e:SettingsManager.getGameWorld(gameID).getEntities()){
         * if(arena.containsBlock(e.getLocation())){ e.remove(); } }
         */
        for (Player p : getPlayers().keySet()) {
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[4]);
            p.updateInventory();
            p.teleport(SettingsManager.getInstance().getLobbySpawn());
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            clearPotions(p);
            p.setFlying(false);
            p.setAllowFlight(false);
            p.setDisplayName(p.getName());
            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
        }
        getPlayers().clear();
        pClasses.clear();
        inactive.clear();
        state = State.LOBBY;
        
    }
    
    /*
     * private int convertHealth(double h) { if (h > 17) { return 1; } else if
     * (h > 14) { return 151; } else if (h > 10) { return 301; } else if (h > 5)
     * { return 601; } else if (h > 2) { return 1001; } else { return -1; } }
     */
    
    public void spawnPlayer(Player p) {
        if (getPlayers().containsKey(p)) {
            p.setAllowFlight(true);
            Random r = new Random();
            Location l = SettingsManager.getInstance().getSpawnPoint(gameID, r.nextInt(spawnCount) + 1);
            p.teleport(getSafePoint(l));
            getPlayerClassBase(p).PlayerSpawn();
        }
        
    }
    
    @SuppressWarnings("deprecation")
    public Location getSafePoint(Location l) {
        if (isInVoid(l)) {
            while (l.getBlockY() < 256) {
                if (l.getBlock().getTypeId() != 0) {
                    return l.add(0, 1, 0);
                }
                else {
                    l.add(0, 1, 0);
                }
            }
        }
        return l;
    }
    
    @SuppressWarnings("deprecation")
    public boolean isInVoid(Location l) {
        Location loc = l.clone();
        while (loc.getBlockY() > 0) {
            loc.add(0, -1, 0);
            if (loc.getBlock().getTypeId() != 0) { return false; }
        }
        return true;
    }
    
    public int getID() {
        return gameID;
    }
    
    public boolean isBlockInArena(Location v) {
        return arena.containsBlock(v);
    }
    
    public void addSpawn() {
        spawnCount++;
    }
    
    public boolean isPlayerActive(Player p) {
        return getPlayers().keySet().contains(p);
    }
    
    public boolean isInQueue(Player p) {
        return queue.contains(p);
    }
    
    public void removeFromQueue(Player p) {
        queue.remove(p);
    }
    
    @SuppressWarnings("deprecation")
    public void removePlayer(Player p) {
        getPlayers().remove(p);
        p.getInventory().clear();
        p.updateInventory();
        clearPotions(p);
        playerEliminate(p);
        inactive.remove(p);
        p.teleport(SettingsManager.getInstance().getLobbySpawn());
        msgAll(ChatColor.RED + p.getName() + " left the game!");
    }
    
    public void msgAll(String msg) {
        for (Player p : getPlayers().keySet()) {
            p.sendMessage(msg);
        }
    }
    
    public void enable() {
        if (state != State.DISABLED) {
            disable();
        }
        state = State.LOBBY;
    }
    
    public void disable() {
        for (Player p : getPlayers().keySet().toArray(new Player[0])) {
            playerEliminate(p);
            p.sendMessage(ChatColor.RED + "Game Disabled");
        }
        gameEnd();
        state = State.DISABLED;
        
    }
    
    public State getState() {
        return state;
    }
    
    public PlayerClassBase getPlayerClassBase(Player p) {
        return pClasses.get(p);
    }
    
    public Set<Player> getActivePlayers() {
        return getPlayers().keySet();
    }
    
    public HashMap<Player, Integer> getPlayers() {
        return players;
    }
    
    public void setPlayers(HashMap<Player, Integer> players) {
        this.players = players;
    }
    
    public Gameboard getBoard() {
        return b;
    }
    
}
