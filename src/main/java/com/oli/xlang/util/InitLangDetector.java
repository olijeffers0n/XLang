package com.oli.xlang.util;

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.oli.xlang.XLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class InitLangDetector extends Thread{

    private XLang plugin;

    public InitLangDetector(XLang plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            this.plugin.detector = LanguageDetectorBuilder.fromAllSpokenLanguages().build();
            this.plugin.detector.detectLanguageOf("Starting the Language Service");
            this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Language Service Has been Enabled");
        }catch (NoClassDefFoundError ignored) {
            this.plugin.getLogger().severe("______________________________________________");
            this.plugin.getLogger().severe("YOU DO NOT HAVE THE LANGUAGE LIBRARY AVAILABLE");
            this.plugin.getLogger().severe("You are Running: " + Bukkit.getVersion() + ", You must have <= 1.16.5");
            this.plugin.getLogger().severe("If you are running 1.16.5 you may need to update your build as you do not have the 'libraries' feature");
            this.plugin.getLogger().severe("______________________________________________");
        }
    }
}
