package com.oli.xlang.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SelectNewLanguageEvent extends Event {

    private String languageCode;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SelectNewLanguageEvent(String languageCode) {
        this.languageCode = languageCode;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }
}
