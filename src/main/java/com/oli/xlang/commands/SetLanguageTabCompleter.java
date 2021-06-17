package com.oli.xlang.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetLanguageTabCompleter implements TabCompleter {


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("setlanguage")) {
            if (args.length == 1) {
                List<String> argStrings = new ArrayList<>();
                argStrings.add("reset");
                argStrings.add("setLanguage");
                return argStrings;
            }
        }
        return null;
    }
}
