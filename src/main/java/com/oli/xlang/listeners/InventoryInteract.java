package com.oli.xlang.listeners;

import com.oli.xlang.XLang;
import com.oli.xlang.events.SelectNewLanguageEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryInteract implements Listener {

    private XLang plugin;

    public InventoryInteract(XLang plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Choose a Language:")) return;
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        event.setCancelled(true);
        if (item.getType() != Material.PLAYER_HEAD) return;
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if (!this.plugin.headCodes.containsKey(name)) return;
        Bukkit.getPluginManager().callEvent(new SelectNewLanguageEvent(name));
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Choose a Language:")) return;
        event.setCancelled(true);
    }
}
