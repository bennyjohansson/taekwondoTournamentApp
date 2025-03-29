package com.taekwondo.tournament.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Taekwondo club in the tournament system.
 * Each club can have multiple participants and participates in tournaments.
 * Features:
 * - Unique identifier for database persistence
 * - Required club name for identification
 * - Optional location information
 * - Bidirectional relationship with participants
 * - JSON serialization handling for circular references
 */
@Entity
@Table(name = "clubs")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Club name is required")
    private String name;

    private String location;

    /**
     * List of participants affiliated with this club.
     * Managed bidirectionally with cascade operations.
     */
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("club")
    private List<Participant> participants = new ArrayList<>();
} 