package com.oli.xlang.commands;

import com.oli.xlang.XLang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XLangCommand implements CommandExecutor {

    private final XLang plugin;
    private final Set<String> validLanguages = new HashSet<>(Arrays.asList("EL","SV","DE","ES","DA","RU","SK","IT","LT","HU","NL","FI","ZH","JA","ET","SL","EN-GB","PT-PT","GB","PL","RO","FR","CS","LV"));

    public XLangCommand(XLang plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args) {

        if (!command.getName().equalsIgnoreCase("xlang")) return false;

        if(args.length == 0) return true;
        // Send the help message

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("XLang.reload")) {
                this.plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "The Plugin Config has been reloaded!");
                return true;
            }else{
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("setTranslationMode")) {
            if (sender.hasPermission("Xlang.setmode")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("serverWide")) {
                        this.plugin.getConfig().set("perPlayerLanguage", false);
                    } else if (args[1].equalsIgnoreCase("perPlayerLocale")) {
                        this.plugin.getConfig().set("perPlayerLanguage", true);
                    } else {
                        sender.sendMessage(ChatColor.RED + "A Translation mode argument is required");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "The Translation mode has been changed to: " + args[1]);
                    this.plugin.saveConfig();
                    this.plugin.reloadConfig();
                } else {
                    sender.sendMessage(ChatColor.RED + "A Translation mode argument is required");
                }
                return true;

            }else {
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        }else if (args[0].equalsIgnoreCase("setTargetLanguage")) {
            if (sender.hasPermission("Xlang.setlanguage")) {
                if (args.length == 2) {
                    if (this.validLanguages.contains(args[1])) {
                        this.plugin.getConfig().set("targetLanguageCode", args[1]);
                        this.plugin.saveConfig();
                        this.plugin.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "The Language has been set!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "That is not a valid language, please use one of the languages provided.");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "A Language argument is required");
                    return true;
                }
            }else {
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        }

        return true;
    }




    private void sendHelpMessage(CommandSender sender) {

    }
}
