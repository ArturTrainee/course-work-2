package com.geekhub.secretaryhelperapp.event.model;

public enum EventType {

    MEETING("meeting"), CONFERENCE("conference"), BUSINESS_TRIP("business trip");

    public final String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType valueOfType(String type) {
        for (final var e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }

}
