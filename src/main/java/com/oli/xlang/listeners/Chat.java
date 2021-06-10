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

import java.util.*;

public class Chat implements Listener {

    private final XLang plugin;
    private final Set<String> validLocales = new HashSet<>(Arrays.asList("el","sv","de","es","da","ru","sk","it","lt","hu","nl","fi","zh","ja","et","sl","pl","ro","fr","cs","lv"));

    public Chat(XLang plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player sender = event.getPlayer();
        String message = event.getMessage();

        String targetColour = this.plugin.getConfig().getString("messageTranslatedColour");
        String sourceColour = this.plugin.getConfig().getString("ownMessageTranslatedColour");


        if (this.plugin.getConfig().getBoolean("perPlayerLanguage")) {
            Set<Player> recipients = event.getRecipients();

            event.setCancelled(true);

            Set<String> playerLocales = new HashSet<>();
            recipients.forEach( player -> playerLocales.add(player.getLocale()));

            Map<String, String> messagesPerLocale = this.getAllLocaleTranslation(playerLocales, message);

            recipients.forEach( player -> {
                TextComponent textComponent;
                String colour;
                if (messagesPerLocale.containsKey(player.getLocale())) {
                    if (player.getUniqueId().equals(sender.getUniqueId())) colour = sourceColour;
                    else colour = targetColour;
                    textComponent = this.textComponentBuilder(message, messagesPerLocale.get(player.getLocale()), sender.getDisplayName(), colour, true);
                } else {
                    textComponent = this.textComponentBuilder(message, message, sender.getDisplayName(), targetColour, false);
                }
                player.spigot().sendMessage(textComponent);
            });
            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(new ChatTranslateEvent(message, messagesPerLocale)));

        }else {
            Language language = this.plugin.detector.detectLanguageOf(message);
            String sourceLanguage = this.plugin.languageCodes.get(language.name().toLowerCase());
            if (sourceLanguage != null)
                if (sourceLanguage.equalsIgnoreCase(this.plugin.getConfig().getString("targetLanguageCode"))) return;

            event.setCancelled(true);

            String translation = this.getTranslationForText(message, this.plugin.getConfig().getString("targetLanguageCode"));

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
        }
        this.plugin.getLogger().info("<" + event.getPlayer().getDisplayName() + "> " + message);
    }

    private TextComponent textComponentBuilder(String original, String translated, String playerName, String colour, Boolean hasBeenTranslated) {

        TextComponent textComponent = new TextComponent("<" + playerName + "> " + translated);
        if (hasBeenTranslated) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "The Message Has been translated, here is the original: \n" + original)));
            textComponent.setColor(net.md_5.bungee.api.ChatColor.of(colour));
        }
        else textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "We Unfortunately do not have your locale so could not translate the message")));

        return textComponent;
    }

    private Map<String, String> getAllLocaleTranslation(Set<String> locales, String message){

        Map<String, String> translations = new HashMap<>();

        locales.forEach( locale -> {
            String isoKey = getDeeplCode(locale);
            if (!isoKey.equals("")) translations.put(locale, this.getTranslationForText(message, isoKey));
        });

        return translations;

    }

    private String getTranslationForText(String input, String language) {
        return StringUtils.capitalize(this.plugin.translator.getTranslation(input, language));
    }

    private String getDeeplCode(String locale) {
        String countryCode = locale.split("_")[0];

        if (countryCode.equalsIgnoreCase("en")) return "EN-GB";
        else if (countryCode.equalsIgnoreCase("bg")) return "GB";
        else if (countryCode.equalsIgnoreCase("pt")) return "PT-PT";
        else if (this.validLocales.contains(countryCode)) return countryCode.toUpperCase();
        else return "";
    }
}
