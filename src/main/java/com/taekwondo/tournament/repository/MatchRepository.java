package com.taekwondo.tournament.repository;

import com.taekwondo.tournament.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findByTournamentIdAndRound(Long tournamentId, String round);
    List<Match> findByTournamentIdAndMatNumber(Long tournamentId, Integer matNumber);
    List<Match> findByParticipant1IdOrParticipant2Id(Long participant1Id, Long participant2Id);
} 