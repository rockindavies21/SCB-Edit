package com.gmail.Jacob6816.scb.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadCreator {
    public HeadCreator() {}
    
    public ItemStack getPlayerhead(String name, String display) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(display);
        skull.setItemMeta(meta);
        return skull;
    }
}
