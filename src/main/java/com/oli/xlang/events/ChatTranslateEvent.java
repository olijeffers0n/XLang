package com.oli.xlang.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChatTranslateEvent extends Event {


    private final String message;
    private Map<String, String> translations;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public ChatTranslateEvent(String message, Map<String, String> translations){
        this.message = message;
        this.translations = translations;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getTranslations() {
        return this.translations;
    }
}
