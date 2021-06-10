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
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        String ownMessageTranslatedColour = this.plugin.getConfig().getString("");
        String messageTranslatedColour = this.plugin.getConfig().getString("");

        System.out.println(ownMessageTranslatedColour);

        if (plugin.getConfig().getBoolean("perPlayerLanguage")) {

            event.setCancelled(true);

            Set<String> locales = new HashSet<>();

            Bukkit.getOnlinePlayers().forEach(player -> locales.add(player.getLocale()));

            Map<String, String> translatedMessages = this.getAllLocaleTranslation(locales, message);

            Bukkit.getOnlinePlayers().forEach(player -> {

                TextComponent textComponent;
                if (translatedMessages.containsKey(player.getLocale())) {

                    String returnedTranslation = translatedMessages.get(player.getLocale());

                    if (player.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                        textComponent = textComponentBuilder(returnedTranslation, message, event.getPlayer().getDisplayName(), ownMessageTranslatedColour, true);
                    }
                    else{
                        textComponent = textComponentBuilder(returnedTranslation, message, event.getPlayer().getDisplayName(), messageTranslatedColour, true);
                    }
                }else {
                    textComponent = textComponentBuilder(message, message, event.getPlayer().getDisplayName(), messageTranslatedColour, false);
                }

                player.spigot().sendMessage(textComponent);

            });

            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(new ChatTranslateEvent(message, translatedMessages)));

        }else {

            Language language = this.plugin.detector.detectLanguageOf(message);

            String sourceLanguage = this.plugin.languageCodes.get(language.name().toLowerCase());
            if (sourceLanguage != null)
                if (sourceLanguage.equalsIgnoreCase(this.plugin.getConfig().getString("targetLanguageCode"))) return;

            event.setCancelled(true);

            String finalTranslation = this.plugin.translator.getTranslation(message.toLowerCase(), this.plugin.getConfig().getString("targetLanguageCode"));

            if (message.equalsIgnoreCase(finalTranslation)) {
                event.setCancelled(false);
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                TextComponent textComponent;
                if (player.getUniqueId().equals(event.getPlayer().getUniqueId()))
                     textComponent = textComponentBuilder(finalTranslation, message, event.getPlayer().getDisplayName(), ownMessageTranslatedColour, true);
                else
                    textComponent = textComponentBuilder(finalTranslation, message, event.getPlayer().getDisplayName(), messageTranslatedColour, true);

                player.spigot().sendMessage(textComponent);

            }

            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(new ChatTranslateEvent(message, Collections.singletonMap(language.name(), finalTranslation))));


        }

        this.plugin.getLogger().info("<" + event.getPlayer().getDisplayName() + "> " + message);

    }

    private TextComponent textComponentBuilder(String translated, String original,String name, String colour, Boolean hasBeenTranslated) {

        TextComponent textComponent = new TextComponent("<" + name + "> " + translated);
        if (hasBeenTranslated) textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "The Message Has been translated, here is the original: \n" + original)));
        textComponent.setColor(net.md_5.bungee.api.ChatColor.of(colour));

        return textComponent;
    }

    private Map<String, String> getAllLocaleTranslation(Set<String> locales, String message){

        Map<String, String> translations = new HashMap<>();

        locales.forEach( locale -> {
            String isoKey = getDeeplCode(locale);
            if (!isoKey.equals("")) translations.put(locale, StringUtils.capitalize(this.plugin.translator.getTranslation(message.toLowerCase(), isoKey)));

        });

        return translations;

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
