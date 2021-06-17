package com.oli.xlang.headapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.oli.xlang.XLang;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class HeadApiInterface {

    private XLang plugin;

    public HeadApiInterface(XLang plugin) {
        this.plugin = plugin;
    }

    public ItemStack getHead(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        if (url == null || url.isEmpty())
            return null;

        ItemMeta skullMeta = skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;

        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        profileField.setAccessible(true);

        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skull.setItemMeta(skullMeta);
        return skull;
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
