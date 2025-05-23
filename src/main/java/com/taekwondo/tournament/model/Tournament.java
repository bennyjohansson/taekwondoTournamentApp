package com.taekwondo.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Min(value = 1, message = "Number of mats must be at least 1")
    private Integer numberOfMats;

    @ElementCollection
    @CollectionTable(name = "tournament_categories",
            joinColumns = @JoinColumn(name = "tournament_id"))
    private Set<TournamentCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<Match> matches = new HashSet<>();

    @Embeddable
    @Data
    public static class TournamentCategory {
        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Min(value = 0)
        private Integer minAge;

        @Min(value = 0)
        private Integer maxAge;

        @Enumerated(EnumType.STRING)
        private SkillLevel skillLevel;
    }
} 