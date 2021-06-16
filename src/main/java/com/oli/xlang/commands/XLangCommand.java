package com.oli.xlang.commands;

import com.oli.xlang.XLang;
import com.oli.xlang.util.GetUsedChars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XLangCommand implements CommandExecutor {

    private final XLang plugin;
    private final Set<String> validLanguages = new HashSet<>(Arrays.asList("EL","SV","DE","ES","DA","RU","SK","IT","LT","HU","NL","FI","ZH","JA","ET","SL","EN-GB","PT-PT","BG","PL","RO","FR","CS","LV"));

    public XLangCommand(XLang plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args) {

        if (!command.getName().equalsIgnoreCase("xlang")) return false;

        if(args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("XLang.reload")) {
                this.plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "The Plugin Config has been reloaded!");
                return true;
            }else{
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("setTranslationMode")) {
            if (sender.hasPermission("Xlang.setmode")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("serverWide")) {
                        this.plugin.getConfig().set("language.perPlayerLanguage", false);
                    } else if (args[1].equalsIgnoreCase("perPlayerLocale")) {
                        this.plugin.getConfig().set("language.perPlayerLanguage", true);
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

            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("setTargetLanguage")) {
            if (sender.hasPermission("Xlang.setlanguage")) {
                if (args.length == 2) {
                    if (this.validLanguages.contains(args[1])) {
                        this.plugin.getConfig().set("language.targetLanguageCode", args[1]);
                        this.plugin.saveConfig();
                        this.plugin.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "The Language has been set!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "That is not a valid language, please use one of the languages provided.");
                    }

                } else {
	                this.plugin.headApiInterface.openFullLanguageInventory((Player) sender);
                }
            	
            }
        } else if (args[0].equalsIgnoreCase("getUsedCharacters")) {
            if (sender.hasPermission("Xlang.getUsedCharacters")) {

                GetUsedChars.CharInfo info = new GetUsedChars(this.plugin).getChars();
                sender.sendMessage(ChatColor.GREEN + "Total Translated Characters: " + info.character_count + "/" + info.character_limit);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission");
                return true;
            }
        }
        sendHelpMessage(sender);
        return true;
    }


    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "XLang Help: \n " +
                ChatColor.GREEN + "/xlang setTargetLanguage -- Sets the target language \n" +
                "/xlang setTranslationMode -- Sets the translation mode \n" +
                "/xlang getUsedCharacters -- Gets the # of total translated characters \n" +
                "/xlang reload -- Reloads the config");
    }
}
