package com.oli.xlang.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XLangTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

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
                        return Arrays.asList("[GUI]", "[DEFAULT]", "EL", "SV", "DE", "ES", "DA", "RU", "SK", "IT", "LT", "HU", "NL", "FI", "ZH", "JA", "ET", "SL", "EN-GB", "EN-US", "PT-PT", "BG", "PL", "RO", "FR", "CS", "LV");
                    }else return new ArrayList<>();
                }
            }
        }
        return null;
    }
}
