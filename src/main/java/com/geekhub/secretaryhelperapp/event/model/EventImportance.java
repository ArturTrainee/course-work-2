package com.geekhub.secretaryhelperapp.event.model;

public enum EventImportance {

    UNIMPORTANT(0), SLIGHTLY_IMPORTANT(1), IMPORTANT(2), FAIRLY_IMPORTANT(3), VERY_IMPORTANT(4), CRUCIALLY_IMPORTANT(5);

    public final int lvl;

    EventImportance(int lvl) {
        this.lvl = lvl;
    }

    public static EventImportance valueOfLvl(int lvl) {
        for (EventImportance e : values()) {
            if (e.lvl == lvl) {
                return e;
            }
        }
        return null;
    }

}
