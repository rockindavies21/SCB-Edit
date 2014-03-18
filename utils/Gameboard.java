package com.gmail.Jacob6816.scb.utils;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.classes.ClassType;
import org.mcsg.double0negative.supercraftbros.classes.PlayerClassBase;

public class Gameboard {
    private final String lives = ChatColor.AQUA + "" + ChatColor.BOLD + "Lives" + ChatColor.RESET;
    private final String r = ChatColor.RESET.toString();
    private Game game;
    private Scoreboard board = null;
    private ClassType[] classes;
    
    public Gameboard(Game game) {
        this.game = game;
        classes = ClassType.values();
        setup(true);
    }
    
    public void setup(boolean teams) {
        if (board == null) board = Bukkit.getScoreboardManager().getNewScoreboard();
        if (board.getObjective(ChatColor.stripColor(lives)) == null) board.registerNewObjective(ChatColor.stripColor(lives), "dummy");
        if (board.getObjective("health") == null) {
            board.registerNewObjective("health", "health");
            board.clearSlot(DisplaySlot.BELOW_NAME);
            board.getObjective("health").setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "\u2665");
            board.getObjective("health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        board.getObjective(ChatColor.stripColor(lives)).setDisplayName(lives);
        board.clearSlot(DisplaySlot.SIDEBAR);
        board.getObjective(ChatColor.stripColor(lives)).setDisplaySlot(DisplaySlot.SIDEBAR);
        for (ClassType t : classes) {
            String team = localeCaps(t.toString());
            if (board.getTeam(team) == null) board.registerNewTeam(team);
            board.getTeam(team).setAllowFriendlyFire(true);
            board.getTeam(team).setCanSeeFriendlyInvisibles(false);
            board.getTeam(team).setSuffix(ChatColor.RESET + "");
            board.getTeam(team).setPrefix(getPrefix(t));
        }
        registerDeadTeam();
        if (teams) reloadTeams();
        reloadLives();
        loadInPlayers();
    }
    
    private void loadInPlayers() {
        if (game.getActivePlayers().size() <= 0) return;
        for (Player p : game.getActivePlayers()) {
            p.setScoreboard(board);
        }
    }
    
    private void reloadTeams() {
        if (board.getTeams().size() <= 0) {
            setup(true);
            return;
        }
        for (Team t : board.getTeams()) {
            if (t.getPlayers().size() >= 1) {
                Iterator<OfflinePlayer> iter = t.getPlayers().iterator();
                while (iter.hasNext()) {
                    t.removePlayer(iter.next());
                }
            }
        }
        if (game.getActivePlayers().size() == 0) return;
        for (Player p : game.getActivePlayers()) {
            PlayerClassBase c = game.getPlayerClassBase(p);
            if (c != null) {
                Team t = board.getTeam(localeCaps(c.getType().toString()));
                t.addPlayer(p);
            }
        }
    }
    
    private void reloadLives() {
        if (game.getActivePlayers().size() >= 1) {
            for (Player p : game.getActivePlayers()) {
                board.resetScores(p);
                int i = game.getPlayers().get(p);
                board.getObjective(DisplaySlot.SIDEBAR).getScore(p).setScore(i);
                if (i <= 0) board.resetScores(p);
            }
        }
    }
    
    private String localeCaps(String in) {
        if (in.length() <= 1) return in.toUpperCase();
        String t = in.toLowerCase();
        char c = t.charAt(0);
        String s = String.valueOf(c).toUpperCase();
        return s + t.substring(1);
        
    }
    
    public void hidePlayer(OfflinePlayer p) {
        try {
            board.resetScores(p);
        }
        catch (NullPointerException e) {
            
        }
    }
    
    private String getPrefix(ClassType t) {
        String s = t.getColor() + "[" + localeCaps(t.toString()) + "]" + r;
        return s.length() > 16 ? s.substring(0, 13) + "]" + r : s;
    }
    
    public void setAsDead(Player p) {
        clearFromTeams(p);
        board.getTeam("Dead").getPlayers().add(p);
        board.getObjective(DisplaySlot.SIDEBAR).getScore(p).setScore(0);
    }
    
    private boolean clearFromTeams(Player player) {
        if (board == null || board.getTeams().size() == 0) setup(true);
        for (Team t : board.getTeams()) {
            if (t.getPlayers().contains(player)) {
                t.getPlayers().remove(player);
                return true;
            }
        }
        return false;
    }
    
    private void registerDeadTeam() {
        if (board.getTeam("Dead") == null) board.registerNewTeam("Dead");
        Team t = board.getTeam("Dead");
        t.setAllowFriendlyFire(false);
        t.setCanSeeFriendlyInvisibles(true);
        t.setPrefix(ChatColor.RED + "[DEAD]" + ChatColor.RESET);
        t.setSuffix(ChatColor.RESET + "");
    }
}
