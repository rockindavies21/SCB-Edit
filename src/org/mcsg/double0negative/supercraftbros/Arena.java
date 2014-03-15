package org.mcsg.double0negative.supercraftbros;

import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Arena {
    
    Location min;
    Location max;
    Selection sel;
    
    public Arena(Location min, Location max) {
        this.max = max;
        this.min = min;
        sel = new CuboidSelection(min.getWorld(), min, max);
    }
    
    public boolean containsBlock(Location v) {
        if (!v.getWorld().equals(min.getWorld())) return false;
        return sel.contains(v);
    }
    
    public Location getMax() {
        return max;
    }
    
    public Location getMin() {
        return min;
    }
}
