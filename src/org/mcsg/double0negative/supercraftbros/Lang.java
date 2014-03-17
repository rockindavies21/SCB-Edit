package org.mcsg.double0negative.supercraftbros;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
* @author gomeow
*/
public enum Lang {
    TITLE("title-name", "&4[&fSCB&4]:"),
    PLAYER_JOIN("player-join", "&6%p Joined the game!"),
    PLAYER_MSG_JOIN("player-msg-all-join", "%p Joined the game choose a class!"),
    PLAYER_LEAVE("player-leave", "&b&l%p Left!"),
    PLAYER_ELIMINATED("player-eliminated", "&4%p Lost the game!"),
    DIDNT_PICK_A_CLASS("no-class", "&cYou didnt pick a class."),
    NO_PERMS("no-permissions", "&cYou don''t have permission for that!");
 
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
 