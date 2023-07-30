package com.example.notewizard.controllers;

import javafx.event.Event;
import javafx.event.EventType;

public class ViewChangeEvent extends Event {
    public static final EventType<ViewChangeEvent> VIEW_CHANGE_EVENT = new EventType<>(Event.ANY, "VIEW_CHANGE_EVENT");

    private final String viewName;

    public ViewChangeEvent(String viewName) {
        super(VIEW_CHANGE_EVENT);
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
