package com.oli.xlang.listeners;

import org.bukkit.ChatColor;
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
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	if (plugin.getConfig().getBoolean("chat.enableJoinMessage"))
    	{
    		long delay = 0;

    		if (plugin.getConfig().getBoolean("chat.translateJoinMessage"))
    		{
    			// rough estimate of how long it might take to retrieve locale data
    			// md_5 says 5 sec but we know better
    			delay = 3 * 20;
    		}
    		
			this.plugin.getServer().getScheduler().runTaskLater( this.plugin, new Runnable()
	        {
	            public void run()
	            {
	        		String message = plugin.getConfig().getString("chat.joinMessage");
	        		
	        		message = message.replaceAll("<lang>", plugin.getConfig().getString("language.targetLanguageCode"));

	        		if (plugin.getConfig().getBoolean("chat.translateJoinMessage"))
	        		{
		    			String locale = event.getPlayer().getLocale();
		    			locale = plugin.translator.getDeeplCode(locale);
		    			
		    			message = plugin.translator.getTranslation(message, locale);
	        		}
	        		
	    			event.getPlayer().sendMessage(ChatColor.BLUE + message);
	            }
	        }, delay); // Wait before client sends locale data
    	}
    }
}
