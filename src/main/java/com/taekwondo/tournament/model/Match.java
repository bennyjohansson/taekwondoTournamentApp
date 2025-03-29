package com.taekwondo.tournament.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String matchId; // Format: "mat{matNumber}-match{matchNumber}"

    @NotNull(message = "Mat number is required")
    private Integer matNumber;

    @NotNull(message = "Match order is required")
    private Integer matchOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant1_id")
    @JsonIgnoreProperties({"matches", "club"})
    private Participant participant1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant2_id")
    @JsonIgnoreProperties({"matches", "club"})
    private Participant participant2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    @JsonIgnoreProperties({"matches", "club"})
    private Participant winner;

    @NotNull(message = "Round is required")
    @Enumerated(EnumType.STRING)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    @JsonIgnoreProperties("matches")
    private Tournament tournament;

    private LocalDateTime scheduledTime;

    public enum Round {
        QUARTER_FINAL, SEMI_FINAL, FINAL
    }
} 