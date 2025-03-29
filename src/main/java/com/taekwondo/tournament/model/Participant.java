package com.taekwondo.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a participant in the Taekwondo tournament.
 * Contains personal information, skill level, and club affiliation.
 * Implements validation rules for data integrity:
 * - Name must be between 2 and 100 characters
 * - Age must be between 4 and 100 years
 * - Gender and skill level are required
 * - Club affiliation is managed through a bidirectional relationship
 */
@Entity
@Table(name = "participants")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Min(value = 4, message = "Age must be at least 4 years")
    @Max(value = 100, message = "Age must be less than 100 years")
    private Integer age;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Skill level is required")
    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonIgnoreProperties("participants")
    private Club club;
} 