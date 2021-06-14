package com.oli.xlang.util;

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.oli.xlang.XLang;
import org.bukkit.ChatColor;

public class InitLangDetector extends Thread{

    private XLang plugin;

    public InitLangDetector(XLang plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.detector = LanguageDetectorBuilder.fromAllSpokenLanguages().build();
        this.plugin.detector.detectLanguageOf("Starting the Language Service");
        this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Language Service Has been Enabled");
    }
}
