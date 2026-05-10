package com.yampej.module;

public enum Category {
    COMBAT("Combat"),
    RENDER("Render"),
    MISC("Misc"),
    HUD("HUD");

    public final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
