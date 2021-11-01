package com.oli.xlang.util;

import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import com.oli.xlang.XLang;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Locale;

public class EssentialsManager {

    public static String getEssentialsMessage(Player player, String message) {

        if (XLang.essentials == null) {
            return getDefaultMessage(player,message);
        }

        final User user = XLang.essentials.getUser(player);

        if (user == null) {
            return getDefaultMessage(player, message);
        }

        final String group = user.getGroup();
        final String world = user.getWorld().getName();
        final String username = user.getName();
        final String nickname = user.getFormattedNickname();

        final String prefix = FormatUtil.replaceFormat(XLang.essentials.getPermissionsHandler().getPrefix(player));
        final String suffix = FormatUtil.replaceFormat(XLang.essentials.getPermissionsHandler().getSuffix(player));
        final Team team = player.getScoreboard().getPlayerTeam(player);

        String format = XLang.essentials.getSettings().getChatFormat(group);
        format = format.replace("%1$s", player.getDisplayName());
        format = format.replace("%2$s", message);
        format = format.replace("{0}", group);
        format = format.replace("{1}", XLang.essentials.getSettings().getWorldAlias(world));
        format = format.replace("{2}", world.substring(0, 1).toUpperCase(Locale.ENGLISH));
        format = format.replace("{3}", team == null ? "" : team.getPrefix());
        format = format.replace("{4}", team == null ? "" : team.getSuffix());
        format = format.replace("{5}", team == null ? "" : team.getDisplayName());
        format = format.replace("{6}", prefix);
        format = format.replace("{7}", suffix);
        format = format.replace("{8}", username);
        format = format.replace("{9}", nickname == null ? username : nickname);

        return format;

    }

    private static String getDefaultMessage(Player player, String message) {
        return "<" + player.getDisplayName() + "> "+ message;
    }
}
