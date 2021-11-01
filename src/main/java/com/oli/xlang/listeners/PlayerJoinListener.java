package com.oli.xlang.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.oli.xlang.XLang;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoinListener implements Listener {

	private final XLang plugin;

	public PlayerJoinListener(XLang plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("chat.enableJoinMessage")) {
			long delay = this.plugin.getConfig().getInt("chat.joinMessageDelay");
			if (delay < 60) delay = 60;

			this.plugin.getServer().getScheduler().runTaskLater( this.plugin, () -> {
				String message = plugin.getConfig().getString("chat.joinMessage");

				if (plugin.getConfig().getBoolean("language.perPlayerLanguage")) {
					String locale = this.getPlayersLanguage(player);
					if (locale.equalsIgnoreCase("ERROR"))
						message = message.replaceAll("<lang>", "|| Sorry, But we do not support your language ||");
					else
						message = message.replaceAll("<lang>", locale);
				}else
					message = message.replaceAll("<lang>", plugin.getConfig().getString("language.targetLanguageCode"));


				if (plugin.getConfig().getBoolean("chat.translateJoinMessage")) {
					String locale = this.getPlayersLanguage(player);
					if (!locale.equalsIgnoreCase("ERROR")) {
						message = plugin.translator.getTranslation(message, locale);
					}
				}

				player.sendMessage(ChatColor.BLUE + message);

			}, delay); // Wait before client sends locale data
		}
	}

	private String getPlayersLanguage(Player player) {
		String playerLocale;
		if (player.getPersistentDataContainer().has(this.plugin.key, PersistentDataType.STRING)) {
			playerLocale = player.getPersistentDataContainer().get(this.plugin.key, PersistentDataType.STRING);
		}else
			playerLocale = player.getLocale();
		return this.plugin.translator.getDeeplCode(playerLocale);
	}
}