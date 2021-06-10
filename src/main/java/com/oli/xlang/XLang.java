package com.oli.xlang;

import com.github.pemistahl.lingua.api.LanguageDetector;
import com.oli.xlang.commands.XLangCommand;
import com.oli.xlang.commands.XLangTabCompleter;
import com.oli.xlang.listeners.Chat;
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
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Starting the Language Service");
        this.initLangDetector = new InitLangDetector(this);
        this.initLangDetector.start();
    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Chat(this), this);
        this.parameterStringBuilder = new ParameterBuilder();
        this.translator = new Translator(this);

        loadConfig();

        try {
            this.url = new URL("https://api-free.deepl.com/v2/translate");
        } catch (MalformedURLException e) {
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
        getCommand("xlang").setTabCompleter(new XLangTabCompleter(this));

    }

    private void loadConfig() {
        getConfig().options().header("Change the config settings below");
        getConfig().addDefault("apiKey", "XXX");
        getConfig().addDefault("targetLanguageCode", "EN-GB");
        getConfig().addDefault("perPlayerLanguage", false);
        getConfig().addDefault("ownMessageTranslatedColour", "#63d3ff");
        getConfig().addDefault("messageTranslatedColour", "#a1ffb0");
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

    //fae7b9b3-b700-7955-4597-5c5b2eeb1517:fx
}
