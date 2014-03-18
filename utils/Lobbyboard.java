package com.gmail.Jacob6816.scb.utils;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mcsg.double0negative.supercraftbros.Game;

public class Lobbyboard {
    private final String waiting = ChatColor.AQUA + "" + ChatColor.BOLD + "Waiting:" + ChatColor.RESET;
    private final String needed = ChatColor.RED + "" + ChatColor.BOLD + "Needed:" + ChatColor.RESET;
    private Game game;
    private Scoreboard board = null;
    
    public Lobbyboard(Game game) {
        this.game = game;
        setup(true);
    }
    
    public void setup(boolean players) {
        if (board == null) board = Bukkit.getScoreboardManager().getNewScoreboard();
        String objective = "Arena " + game.getID();
        if (board.getObjective(objective) == null) board.registerNewObjective(ChatColor.stripColor(objective), "dummy");
        board.getObjective(objective).setDisplayName(ChatColor.GREEN + objective);
        board.clearSlot(DisplaySlot.SIDEBAR);
        board.getObjective(objective).setDisplaySlot(DisplaySlot.SIDEBAR);
        String team = "Waiting";
        if (board.getTeam(team) == null) board.registerNewTeam(team);
        board.getTeam(team).setAllowFriendlyFire(true);
        board.getTeam(team).setCanSeeFriendlyInvisibles(false);
        board.getTeam(team).setSuffix(ChatColor.RESET + "");
        board.getTeam(team).setPrefix(waiting.substring(0, waiting.indexOf(":")) + ChatColor.RESET);
        if (players) loadInPlayers();
    }
    
    private void loadInPlayers() {
        wipeTeams();
        if (game.getPlayers().keySet().size() <= 0) return;
        for (Player p : game.getPlayers().keySet()) {
            board.getTeam("Waiting").getPlayers().add(p);
            p.setScoreboard(board);
        }
    }
    
    private void wipeTeams() {
        if (board == null) setup(false);
        if (board.getTeams().size() <= 0) setup(false);
        for (Team t : board.getTeams()) {
            if (t.getPlayers().size() >= 1) {
                Iterator<OfflinePlayer> iter = t.getPlayers().iterator();
                while (iter.hasNext())
                    t.getPlayers().remove(iter.next());
            }
        }
    }
    
    public void updateScores() {
        if (board == null) setup(true);
        if (board.getObjective(DisplaySlot.SIDEBAR) == null) setup(true);
        Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
        obj.getScore(Bukkit.getOfflinePlayer(waiting)).setScore(board.getTeam("Waiting").getPlayers().size());
        obj.getScore(Bukkit.getOfflinePlayer(needed)).setScore(4 - board.getTeam("Waiting").getPlayers().size());
    }
}
