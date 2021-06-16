package com.oli.xlang.headapi;

import com.oli.xlang.XLang;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeadApiInterface {

    private XLang plugin;

    public HeadApiInterface(XLang plugin) {
        this.plugin = plugin;
    }

    public ItemStack getHead(String mojangUrl) {
        return SkullCreator.itemFromUrl(mojangUrl);
    }

    public void openFullLanguageInventory(Player player, String server) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Choose a "+ server + "Language:");
        for (String key : this.plugin.headCodes.keySet()) {
            ItemStack head = getHead(this.plugin.headCodes.get(key));
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + key);
            head.setItemMeta(meta);
            inventory.addItem(head);
        }
        player.openInventory(inventory);
    }
}
