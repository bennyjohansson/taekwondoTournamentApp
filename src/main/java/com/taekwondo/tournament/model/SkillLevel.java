package com.taekwondo.tournament.model;

/**
 * Represents the skill level/belt rank of a participant in Taekwondo.
 * Follows the traditional Taekwondo belt progression system from White to Black belt.
 * Used for:
 * - Categorizing participants in competitions
 * - Determining match eligibility
 * - Tournament division assignments
 */
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

    /**
     * Gets the human-readable display name of the skill level.
     * @return The formatted display name of the belt rank
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a display name to its corresponding SkillLevel enum value.
     * @param displayName The display name to convert (e.g., "Black Belt")
     * @return The corresponding SkillLevel enum value
     * @throws IllegalArgumentException if no matching skill level is found
     */
    public static SkillLevel fromDisplayName(String displayName) {
        for (SkillLevel level : values()) {
            if (level.displayName.equals(displayName)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid skill level: " + displayName);
    }
} 