package org.mcsg.double0negative.supercraftbros;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
* @author gomeow
*/
public enum Lang {
    TITLE("title-name", "&4[&bSCB&4]"),
    PLAYER_JOIN("player-join", "&2&lYOU &bJoined the game!"),
    PLAYER_MSG_JOIN("player-msg-all-join", "%p Joined the game choose a class! &b[&2+&b]"),
    SELF_LEAVE("self-leave", "You Left!"),
    PLAYER_LEAVE("player-leave", "&b&l%p Left!"),
    PLAYER_ELIMINATED("player-eliminated", "&4%p Lost the game!"),
    DIDNT_PICK_A_CLASS("no-class", "&cYou didnt pick a class."),
    NO_PERMS("no-permissions", "&4&lYou don't have permission for that!"),
    GAME_FULL("cannot-join-game-full", "&4GAME IS FULL!"),
    GAME_STARTED("cannot-join-game-started", "&c&lGAME ALREADY STARTED!"),
    GAME_CANNOT("cannot-join-game", "&c&lCANNOT JOIN GAME"),
    NO_CLASS("kicked-from-game-no-class", "&cYou have been kicked for not choosing a class! D:"),
    COUNTDOWN("count-down", "&bGame starting in %c"),
    CHOOSE_CLASS("class-chosen", "&6You chose %class"),
    LIVES_LEFT("lives-left", "You have %lives Left!"),
    PLAYER_ELIMINATE("player-eliminated", "%p Has been eliminated!"),
    BROADCAST_WIN("player-win-broadcast", "%p Has won on %arena"),
    NOT_ENOUGH("not-enough-players", "&cNOT ENOUGH PLAYERS");
    
    private String path;
    private String def;
    private static YamlConfiguration LANG;
 
    /**
    * Lang enum constructor.
    * @param path The string path.
    * @param start The default string.
    */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }
 
    /**
    * Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }
 
    @Override
    public String toString() {
        if (this == TITLE)
            return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
        	return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
 
    /**
    * Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /**
    * Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }
}
 