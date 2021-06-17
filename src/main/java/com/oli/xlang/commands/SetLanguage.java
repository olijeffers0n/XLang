package com.oli.xlang.commands;

import com.oli.xlang.XLang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLanguage implements CommandExecutor {

    private XLang plugin;

    public SetLanguage(XLang plugin) {
        this.plugin = plugin;}


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            this.plugin.headApiInterface.openFullLanguageInventory((Player) sender, "");
            return true;
        }else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                ((Player) sender).getPersistentDataContainer().remove(this.plugin.key);
                sender.sendMessage(ChatColor.GREEN + "Your Language has been reset - it will now default to your client's locale");
                return true;
            } else if (args[0].equalsIgnoreCase("setLanguage")) {
                this.plugin.headApiInterface.openFullLanguageInventory((Player) sender, "");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "An Argument is required");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Invalid Format");
        return true;
    }
}
