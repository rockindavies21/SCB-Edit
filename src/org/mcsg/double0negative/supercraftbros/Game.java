package org.mcsg.double0negative.supercraftbros;

import java.io.File;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.classes.PlayerClassBase;

import com.gmail.Jacob6816.scb.utils.Gameboard;
import com.gmail.Jacob6816.scb.utils.Lobbyboard;
import com.gmail.Jacob6816.scb.utils.Permissions;
import com.gmail.Jacob6816.scb.utils.PlayerBackup;

public class Game {
    
    public enum State {
        INGAME, LOBBY, DISABLED, WAITING
    }
    
    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    private int gameID;
    private int spawnCount;
    private Arena arena;
    private State state;
    private Gameboard b;
    private Lobbyboard l;
    private HashMap<Player, Integer> players = new HashMap<Player, Integer>();
    private HashMap<Player, PlayerClassBase> pClasses = new HashMap<Player, PlayerClassBase>();
    private HashMap<Player, PlayerBackup> backups = new HashMap<Player, PlayerBackup>();
    private ArrayList<Player> inactive = new ArrayList<Player>();
    
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
        l = new Lobbyboard(this);
        state = State.LOBBY;
        l.setup(true);
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
    
    public boolean signIsForGame(Sign s) {
        String[] l = s.getLines();
        if (l[0] == null) return false;
        if (l[1] == null) return false;
        if (!ChatColor.stripColor(l[0]).equalsIgnoreCase("[scb]")) return false;
        try {
            Integer i = Integer.parseInt(ChatColor.stripColor(l[1]));
            return i.intValue() == getID();
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public void updateSign(Sign s) {
        s.setLine(0, ChatColor.DARK_GREEN + "[SCB]");
        s.setLine(1, ChatColor.AQUA + "" + getID());
        s.setLine(2, ChatColor.GREEN + localeCaps(state.toString()));
        s.setLine(3, ChatColor.BLUE + "" + getActivePlayers().size() + " / " + GameManager.getInstance().maxPlayers);
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
        if (state == State.LOBBY && getPlayers().size() < GameManager.getInstance().maxPlayers) {
            p.setGameMode(GameMode.SURVIVAL);
            PlayerBackup backup = new PlayerBackup(p);
            backup.save();
            backup.wipe();
            backups.put(p, backup);
            p.teleport(SettingsManager.getInstance().getGameLobbySpawn(gameID));
            getPlayers().put(p, GameManager.getInstance().lifeCount);
            p.sendMessage(Lang.TITLE.toString() + Lang.PLAYER_JOIN.toString().replace("%p", p.getName()));
            msgAll(Lang.TITLE.toString() + Lang.PLAYER_MSG_JOIN.toString().replace("%p", p.getName()));
            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
            PlayerBackup.wipeExtPlayer(p);
            if (!started) {
                l.setup(true);
                l.updateScores();
            }
            updateLoadedSigns(p.getWorld(), true);
        }
        else if (state == State.INGAME) {
            p.sendMessage(Lang.TITLE.toString() + Lang.GAME_STARTED);
        }
        else if (getPlayers().size() >= 10) {
            p.sendMessage(Lang.TITLE.toString() + Lang.GAME_FULL);
        }
        else {
            p.sendMessage(Lang.TITLE.toString() + Lang.GAME_CANNOT);
        }
        updateLoadedSigns(p.getWorld(), false);
    }
    
    public void startGame() {
        if (getPlayers().size() < GameManager.getInstance().minPlayers) {
            msgAll(Lang.TITLE.toString() + Lang.NOT_ENOUGH);
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
                p.sendMessage(Lang.TITLE.toString() + Lang.NO_CLASS);
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
                            msgAll(Lang.TITLE.toString() + Lang.COUNTDOWN.toString().replace("%c", String.valueOf(count)));
                        }
                        if (count < 6) {
                            msgAll(Lang.TITLE.toString() + Lang.COUNTDOWN.toString().replace("%c", String.valueOf(count)));
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
        Permissions perms = new Permissions(player);
        if (perms.canUseClass(playerClass.getName())) {
            PlayerBackup.wipeExtPlayer(player);
            player.sendMessage(Lang.TITLE.toString() + Lang.CHOOSE_CLASS.toString().replace("%class", playerClass.getName()));
            pClasses.put(player, playerClass);
            if (!started && pClasses.keySet().size() >= 4 && getPlayers().size() >= 4) {
                countdown(60);
                started = true;
                b.setup(true);
            }
        }
        else {
            player.sendMessage(Lang.TITLE.toString() + Lang.NO_PERMS);
        }
        updateLoadedSigns(player.getWorld(), false);
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
            msgAll(Lang.TITLE.toString() + Lang.LIVES_LEFT.toString().replace("%p", p.getName()).replace("%lives", String.valueOf(lives)));
            b.setup(false);
        }
    }
    
    public void playerEliminate(Player p) {
        started = false;
        msgAll(Lang.TITLE.toString() + Lang.PLAYER_ELIMINATED.toString().replace("%p", p.getName()));
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        getPlayers().remove(p);
        inactive.add(p);
        RestorePlayer(p);
        // TODO fix this: b.setAsDead(p);
        if (getPlayers().keySet().size() <= 1 && state == State.INGAME) {
            Player pl = getPlayers().keySet().toArray(new Player[0])[0];
            Bukkit.broadcastMessage(Lang.TITLE.toString() + Lang.BROADCAST_WIN.toString().replace("%arena", String.valueOf(gameID)).replace("%p", pl.getName()));
            gameEnd();
        }
        p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
        updateLoadedSigns(p.getWorld(), false);
    }
    
    public void clearPotions(Player p) {
        for (PotionEffectType e : PotionEffectType.values()) {
            if (e != null && p.hasPotionEffect(e)) p.removePotionEffect(e);
        }
    }
    
    public void gameEnd() {
        for (Player p : getPlayers().keySet()) {
            RestorePlayer(p);
        }
        getPlayers().clear();
        pClasses.clear();
        inactive.clear();
        backups.clear();
        state = State.LOBBY;
        updateLoadedSigns(Bukkit.getWorlds().get(0), true);
    }
    
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
    
    public void removePlayer(Player p) {
        getPlayers().remove(p);
        RestorePlayer(p);
        msgAll(ChatColor.RED + p.getName() + " left the game!");
        updateLoadedSigns(p.getWorld(), true);
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
    
    /**
     * Gets the lang.yml config.
     * 
     * @return The lang.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }
    
    /**
     * Get the lang.yml file.
     * 
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
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
    
    public PlayerClassBase getPlayerClass(Player p) {
        return pClasses.get(p);
    }
    
    public void setPlayers(HashMap<Player, Integer> players) {
        this.players = players;
    }
    
    public Gameboard getBoard() {
        return b;
    }
    
    public void RestorePlayer(Player player) {
        if (backups.containsKey(player)) {
            backups.get(player).restore();
            backups.remove(player);
        }
    }
}
