package com.oli.xlang.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.oli.xlang.XLang;

public class Join implements Listener {

	private final XLang plugin;

	public Join(XLang plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("chat.enableJoinMessage")) {
			long delay = 0;

			if (plugin.getConfig().getBoolean("chat.translateJoinMessage")) {
				// rough estimate of how long it might take to retrieve locale data
				// md_5 says 5 sec but we know better (of course)
				delay = 3 * 20;
			}

			this.plugin.getServer().getScheduler().runTaskLater( this.plugin, () -> {
				String message = plugin.getConfig().getString("chat.joinMessage");

				if (plugin.getConfig().getBoolean("language.perPlayerLanguage")) {
					if (plugin.getConfig().getBoolean("chat.translateJoinMessage")) {
						String locale = plugin.translator.getDeeplCode(player.getLocale());
						if (locale.equalsIgnoreCase("ERROR")) {
							message = message.replaceAll("<lang>", "|| Sorry, But we do not support your language ||");
						}else {
							message = message.replaceAll("<lang>", locale);
						}
					}else
						message = message.replaceAll("<lang>", plugin.getConfig().getString("language.targetLanguageCode"));
				}else
					message = message.replaceAll("<lang>", plugin.getConfig().getString("language.targetLanguageCode"));


				if (plugin.getConfig().getBoolean("chat.translateJoinMessage")) {
					String locale = player.getLocale();
					locale = plugin.translator.getDeeplCode(locale);
					if (!locale.equalsIgnoreCase("ERROR")) {
						message = plugin.translator.getTranslation(message, locale);
					}
				}

				player.sendMessage(ChatColor.BLUE + message);

			}, delay); // Wait before client sends locale data
		}
	}
}