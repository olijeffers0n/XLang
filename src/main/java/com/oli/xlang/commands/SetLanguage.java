package com.oli.xlang.commands;

import com.oli.xlang.XLang;
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
        this.plugin.headApiInterface.openFullLanguageInventory((Player) sender, "");
        return true;
    }
}
