package com.oli.xlang.commands;

import com.oli.xlang.XLang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XLangTabCompleter implements TabCompleter {

    private XLang plugin;

    public XLangTabCompleter(XLang plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("xlang")) {

            if (args.length == 1) {
                List<String> argStrings = new ArrayList<>();
                argStrings.add("reload");
                argStrings.add("setTranslationMode");
                argStrings.add("setTargetLanguage");
                argStrings.add("getUsedCharacters");
                return argStrings;
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setTranslationMode")) {
                    if (sender.hasPermission("Xlang.setmode")) {
                        List<String> argStrings = new ArrayList<>();
                        argStrings.add("serverWide");
                        argStrings.add("perPlayerLocale");
                        return argStrings;
                    }else return new ArrayList<>();
                }else if (args[0].equalsIgnoreCase("setTargetLanguage")) {
                    if (sender.hasPermission("XLang.setlanguage")) {
                        List<String> argStrings = Arrays.asList("EL", "SV", "DE", "ES", "DA", "RU", "SK", "IT", "LT", "HU", "NL", "FI", "ZH", "JA", "ET", "SL", "EN-GB", "PT-PT", "BG", "PL", "RO", "FR", "CS", "LV");
                        return argStrings;
                    }else return new ArrayList<>();
                }
            }
        }
        return null;
    }
}
