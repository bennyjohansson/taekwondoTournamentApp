package com.taekwondo.tournament.model;

public enum SkillLevel {
    WHITE_BELT("White Belt"),
    YELLOW_BELT("Yellow Belt"),
    GREEN_BELT("Green Belt"),
    BLUE_BELT("Blue Belt"),
    RED_BELT("Red Belt"),
    BLACK_BELT("Black Belt");

    private final String displayName;

    SkillLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SkillLevel fromDisplayName(String displayName) {
        for (SkillLevel level : values()) {
            if (level.displayName.equals(displayName)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid skill level: " + displayName);
    }
} 