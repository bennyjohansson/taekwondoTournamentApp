package com.taekwondo.tournament.model;

/**
 * Represents the gender of a participant in the tournament.
 * Used for categorizing participants in competitions and matches.
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a display name to its corresponding Gender enum value.
     * @param displayName The display name to convert
     * @return The corresponding Gender enum value
     * @throws IllegalArgumentException if no matching gender is found
     */
    public static Gender fromDisplayName(String displayName) {
        for (Gender gender : values()) {
            if (gender.displayName.equals(displayName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender: " + displayName);
    }
} 