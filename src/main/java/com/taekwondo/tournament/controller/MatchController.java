package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Match;
import com.taekwondo.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody Match matchDetails) {
        return ResponseEntity.ok(matchService.updateMatch(id, matchDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<Match>> getMatchesByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/round/{round}")
    public ResponseEntity<List<Match>> getMatchesByTournamentAndRound(
            @PathVariable Long tournamentId,
            @PathVariable String round) {
        return ResponseEntity.ok(matchService.getMatchesByTournamentAndRound(tournamentId, round));
    }

    @GetMapping("/tournament/{tournamentId}/mat/{matNumber}")
    public ResponseEntity<List<Match>> getMatchesByTournamentAndMat(
            @PathVariable Long tournamentId,
            @PathVariable Integer matNumber) {
        return ResponseEntity.ok(matchService.getMatchesByTournamentAndMat(tournamentId, matNumber));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Match>> getMatchesByParticipant(@PathVariable Long participantId) {
        return ResponseEntity.ok(matchService.getMatchesByParticipant(participantId));
    }
} 