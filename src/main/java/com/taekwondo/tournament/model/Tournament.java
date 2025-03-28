package com.taekwondo.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tournament name is required")
    private String name;

    @NotNull(message = "Tournament date is required")
    private LocalDate date;

    @NotNull(message = "Number of mats is required")
    private Integer numberOfMats;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tournament_categories")
    private List<BracketCategory> categories = new ArrayList<>();

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class BracketCategory {
        @Enumerated(EnumType.STRING)
        private Participant.Gender gender;
        
        @Enumerated(EnumType.STRING)
        private Participant.SkillLevel skillLevel;
        
        private String ageGroup; // e.g., "0-11", "12-16", "16-35", "35+"
    }
} 