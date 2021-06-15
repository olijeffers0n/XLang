package com.oli.xlang.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SelectNewLanguageEvent extends Event {

    private String languageCode;
    private String who;
    private UUID uuid;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SelectNewLanguageEvent(String languageCode, String who, UUID uuid) {
        this.languageCode = languageCode;
        this.who = who;
        this.uuid = uuid;
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

    public String getWho() {
        return this.who;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
