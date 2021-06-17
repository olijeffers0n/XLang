package com.oli.xlang.listeners;

import com.github.pemistahl.lingua.api.Language;
import com.oli.xlang.XLang;
import com.oli.xlang.events.ChatTranslateEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Chat implements Listener {

    private final XLang plugin;
    
    public Chat(XLang plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if (this.plugin.getConfig().getString("deepl.apiKey").equalsIgnoreCase("xxx")) {
            this.plugin.getLogger().warning("Deepl API key not set - not attempting to translate");
            return;
        }

        Player sender = event.getPlayer();
        String message = event.getMessage();

        String targetColour = this.plugin.getConfig().getString("colour.messageTranslatedColour");
        String sourceColour = this.plugin.getConfig().getString("colour.ownMessageTranslatedColour");
        String extra;

        if (this.plugin.getConfig().getBoolean("language.perPlayerLanguage")) {
            Set<Player> recipients = event.getRecipients();
            Map<String, Set<UUID>> targetPlayerLanguages = new HashMap<>();

            event.setCancelled(true);

            recipients.forEach( player -> {
                String playerLocale;
                if (player.getPersistentDataContainer().has(this.plugin.key, PersistentDataType.STRING)) {
                    playerLocale = player.getPersistentDataContainer().get(this.plugin.key, PersistentDataType.STRING);
                }else
                    playerLocale = player.getLocale();
                String isoKey = this.plugin.translator.getDeeplCode(playerLocale);

                Set<UUID> uuids = targetPlayerLanguages.getOrDefault(isoKey, new HashSet<>());
                uuids.add(player.getUniqueId());
                targetPlayerLanguages.put(isoKey, uuids);
            });

            Map<String, String> messagesPerLocale = this.sendAllTranslations(targetPlayerLanguages, sender.getUniqueId(), message, sourceColour, targetColour);

            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(new ChatTranslateEvent(message, messagesPerLocale)));

            extra = " || Translated to " + targetPlayerLanguages.size() + " locale/s";

        } else {
            Language language = this.plugin.detector.detectLanguageOf(message);
            String sourceLanguage = this.plugin.languageCodes.get(language.name().toLowerCase());
            if (sourceLanguage != null)
                if (sourceLanguage.equalsIgnoreCase(this.plugin.getConfig().getString("language.targetLanguageCode"))) return;

            event.setCancelled(true);

            String translation = this.getTranslationForText(message, this.plugin.getConfig().getString("language.targetLanguageCode"));

            if (message.equalsIgnoreCase(translation)) {
                event.setCancelled(false);
                return;
            }

            event.getRecipients().forEach(player -> {

                TextComponent textComponent;
                if (player.getUniqueId().equals(sender.getUniqueId()))
                    textComponent = this.textComponentBuilder(message, translation, sender.getDisplayName(), sourceColour, true);
                else
                    textComponent = this.textComponentBuilder(message, translation, sender.getDisplayName(), targetColour, true);
                player.spigot().sendMessage(textComponent);
            });

            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(new ChatTranslateEvent(message, Collections.singletonMap(language.name(), translation))));
            extra = " || Translated to: " + translation;
        }
        if (!this.plugin.getConfig().getBoolean("chat.addXLangTranslationComment")) extra = "";
        this.plugin.getLogger().info("<" + event.getPlayer().getDisplayName() + "> " + message + extra);
    }

    private TextComponent textComponentBuilder(String original, String translated, String playerName, String colour, Boolean hasBeenTranslated) {

        TextComponent textComponent = new TextComponent("<" + playerName + "> " + translated);
        if (hasBeenTranslated) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "The Message Has been translated, here is the original: \n" + original)));
            textComponent.setColor(ChatColor.of(colour));
        }
        else textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "We Unfortunately do not have your locale so could not translate the message")));

        return textComponent;
    }

    private Map<String, String> sendAllTranslations(Map<String, Set<UUID>> languageCodes, UUID senderUUID, String message, String sourceColour, String targetColour) {
        Map<String, String> messagesPerLocale = new HashMap<>();
        for (String language : languageCodes.keySet()) {
            if (!language.equalsIgnoreCase("ERROR")) {
                String translation = this.getTranslationForText(message, language);
                messagesPerLocale.put(language, translation);
                languageCodes.get(language).forEach(uuid -> {

                    Player player = Bukkit.getPlayer(uuid);

                    TextComponent textComponent;
                    String colour;

                    if (player.getUniqueId().equals(senderUUID)) colour = sourceColour;
                    else colour = targetColour;
                    textComponent = this.textComponentBuilder(message, translation, Bukkit.getPlayer(senderUUID).getDisplayName(), colour, true);

                    player.spigot().sendMessage(textComponent);
                });
            }else {
                languageCodes.get(language).forEach(uuid -> {
                    Player player = Bukkit.getPlayer(uuid);
                    TextComponent textComponent = this.textComponentBuilder(message, message, Bukkit.getPlayer(senderUUID).getDisplayName(), targetColour, false);
                    player.spigot().sendMessage(textComponent);
                });
            }
        }

        return messagesPerLocale;
    }

    private String getTranslationForText(String input, String language) {
        return StringUtils.capitalize(this.plugin.translator.getTranslation(input.toLowerCase(), language));
    }
}
