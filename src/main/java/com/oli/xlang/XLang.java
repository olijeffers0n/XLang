package com.oli.xlang;

import com.github.pemistahl.lingua.api.LanguageDetector;
import com.oli.xlang.commands.XLangCommand;
import com.oli.xlang.commands.XLangTabCompleter;
import com.oli.xlang.listeners.Chat;
import com.oli.xlang.listeners.Join;
import com.oli.xlang.translator.Translator;
import com.oli.xlang.util.InitLangDetector;
import com.oli.xlang.util.ParameterBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import java.util.Map;

public class XLang extends JavaPlugin implements Listener {

    public URL url;
    public ParameterBuilder parameterStringBuilder;
    public LanguageDetector detector;
    public InitLangDetector initLangDetector;
    public Translator translator;
    public Map<String, String> languageCodes;

    @Override
    public void onLoad() {

        if (Bukkit.getWorlds().size() != 0) Bukkit.shutdown();

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Starting the Language Service");
        this.initLangDetector = new InitLangDetector(this);
        this.initLangDetector.start();
    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Chat(this), this);
        Bukkit.getPluginManager().registerEvents(new Join(this), this);
        this.parameterStringBuilder = new ParameterBuilder();
        this.translator = new Translator(this);

        loadConfig();

        try {
            if (getConfig().getBoolean("deepl.premiumDeepl")) this.url = new URL("https://api.deepl.com/v2/translate");
            else this.url = new URL("https://api-free.deepl.com/v2/translate");
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            this.languageCodes = new HashMap<>();
            loadLanguageCodes();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            this.initLangDetector.join();
        } catch (InterruptedException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }

        getCommand("xlang").setExecutor(new XLangCommand(this));
        getCommand("xlang").setTabCompleter(new XLangTabCompleter());

    }

    private void loadConfig() {
        getConfig().options().header("Change the config settings below");
        // API Key for DeepL Translator, provided by server owner
        getConfig().addDefault("deepl.apiKey", "XXX");
        // Whether or not the user is using Premium API Access
        getConfig().addDefault("deepl.premiumDeepl", false);
        // Target language for translation service
        getConfig().addDefault("language.targetLanguageCode", "EN-GB");
        // Whether or not the translator should target player locale.
        // Note: Massively increases API usage.
        getConfig().addDefault("language.perPlayerLanguage", false);
        // Translated color to the player
        getConfig().addDefault("colour.ownMessageTranslatedColour", "#63d3ff");
        // Translated color to other players
        getConfig().addDefault("colour.messageTranslatedColour", "#a1ffb0");
        // Whether or not to comment on the translation (after message)
        getConfig().addDefault("chat.addXLangTranslationComment", true);
        // Whether or not to inform players about XLang's presence on the server.
        getConfig().addDefault("chat.enableJoinMessage", true);
        // If the above is enabled, the message to send players.
        getConfig().addDefault("chat.joinMessage", "This server uses XLang to translate all messages to <lang>.");
        // Whether or not to translate the join message to the player's locale.
        getConfig().addDefault("chat.translateJoinMessage", false);

        getConfig().options().copyDefaults(true);

        saveConfig();
        reloadConfig();
    }

    private void loadLanguageCodes() throws IOException, InvalidConfigurationException {
        saveResource("languages.yml", true);
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(getDataFolder() + File.separator + "languages.yml");
        for (String key : configuration.getConfigurationSection("Map").getKeys(false)) {
            this.languageCodes.put(key, configuration.getString("Map."+key));
        }
    }
}
