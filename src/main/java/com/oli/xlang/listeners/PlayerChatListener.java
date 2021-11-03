package com.oli.xlang.listeners;

import com.github.pemistahl.lingua.api.Language;
import com.oli.xlang.XLang;
import com.oli.xlang.util.EssentialsManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerChatListener implements Listener {

    private final XLang plugin;
    
    public PlayerChatListener(XLang plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
            Set<String> targetPlayerLanguages = new HashSet<>();

            event.setCancelled(true);

            recipients.forEach( player -> targetPlayerLanguages.add(this.getPlayersLanguage(player)));

            this.sendAllTranslationsAsync(targetPlayerLanguages, message, sender, sourceColour, targetColour);

            extra = " || Translated to " + targetPlayerLanguages.size() + " locale/s";

        } else {

            if (this.plugin.getConfig().getBoolean("langdetector.enabled")) {
                Language language = this.plugin.detector.detectLanguageOf(message);
                String sourceLanguage = this.plugin.languageCodes.get(language.name().toLowerCase());
                if (sourceLanguage != null)
                    if (sourceLanguage.equalsIgnoreCase(this.plugin.getConfig().getString("language.targetLanguageCode")))
                        return;
            }

            event.setCancelled(true);

            String translation = this.getTranslationForText(message, this.plugin.getConfig().getString("language.targetLanguageCode"), message.equals(message.toLowerCase()));

            if (message.equalsIgnoreCase(translation)) {
                event.setCancelled(false);
                return;
            }

            event.getRecipients().forEach(player -> {

                TextComponent textComponent;
                if (player.getUniqueId().equals(sender.getUniqueId()))
                    textComponent = this.getTextComponent(message, translation, sender, sourceColour, this.plugin.getConfig().getString("language.targetLanguageCode"));
                else
                    textComponent = this.getTextComponent(message, translation, sender, targetColour, this.plugin.getConfig().getString("language.targetLanguageCode"));
                player.spigot().sendMessage(textComponent);
            });

            extra = " || Translated to: " + translation;
        }
        if (!this.plugin.getConfig().getBoolean("chat.addXLangTranslationComment")) extra = "";
        this.plugin.getLogger().info("<" + event.getPlayer().getDisplayName() + "> " + message + extra);
    }

    private TextComponent getTextComponent(String original, String translated, Player sender, String colour, String language) {
        TextComponent textComponent = new TextComponent(EssentialsManager.getEssentialsMessage(sender, translated));
        if (this.plugin.getConfig().getBoolean("chat.hoverMessage")) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "The Message Has been translated to " + language + ", here is the original: \n" + original)));
        }
        textComponent.setColor(ChatColor.of(colour));

        return textComponent;
    }

    private void sendAllTranslationsAsync(Set<String> languageCodes, String message, Player sender, String sourceColour, String targetColour) {
        for (String language : languageCodes) {
            CompletableFuture.supplyAsync(() ->
                getTranslationForText(message, language, message.equals(message.toLowerCase()))).thenAccept((future) ->
                    getPlayersOfLanguage(language).forEach((p) -> this.sendMessage(p, message, future, sender, sourceColour, targetColour, language)));
        }
    }

    private void sendMessage(Player player, String message, String translation, Player sender, String sourceColour, String targetColour, String language) {
        String colour;
        if (player.getUniqueId().equals(sender.getUniqueId())) colour = sourceColour;
        else colour = targetColour;
        TextComponent textComponent = this.getTextComponent(message, translation, sender, colour, language);
        player.spigot().sendMessage(textComponent);
    }

    private Set<Player> getPlayersOfLanguage(String languageCode) {

        Set<Player> targetLanguagePlayers = new HashSet<>();

        Bukkit.getOnlinePlayers().forEach( player -> {
            String isoKey = this.getPlayersLanguage(player);
            if (isoKey.equalsIgnoreCase(languageCode)) targetLanguagePlayers.add(player);
        });

        return targetLanguagePlayers;
    }

    private String getPlayersLanguage(Player player) {
        String playerLocale;
        if (player.getPersistentDataContainer().has(this.plugin.key, PersistentDataType.STRING)) {
            playerLocale = player.getPersistentDataContainer().get(this.plugin.key, PersistentDataType.STRING);
        }else
            playerLocale = player.getLocale();
        return this.plugin.translator.getDeeplCode(playerLocale);
    }

    private String getTranslationForText(String input, String language, Boolean isLowerCase) {
        String translation = this.plugin.translator.getTranslation(input.toLowerCase(), language);
        if (isLowerCase) return translation;
        else return StringUtils.capitalize(translation);
    }
}