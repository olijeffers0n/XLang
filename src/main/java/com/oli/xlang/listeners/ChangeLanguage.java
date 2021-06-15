package com.oli.xlang.listeners;

import com.oli.xlang.XLang;
import com.oli.xlang.events.SelectNewLanguageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class ChangeLanguage implements Listener {

    private XLang plugin;

    public ChangeLanguage(XLang plugin) {
        this.plugin = plugin;
    }

    public void onLanguageChange(SelectNewLanguageEvent event) {
        if (event.getWho().equalsIgnoreCase("player")) {
            Player player = Bukkit.getPlayer(event.getUuid());
            player.getPersistentDataContainer().set(this.plugin.key, PersistentDataType.STRING, event.getLanguageCode());
        }else {  // The other case would be "console" to represent a server wide change
            this.plugin.getConfig().set("language.targetLanguageCode", event.getLanguageCode());
            this.plugin.saveConfig();
            this.plugin.reloadConfig();
        }
    }
}
