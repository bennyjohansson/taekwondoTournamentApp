package com.taekwondo.tournament.service;

import com.taekwondo.tournament.model.Match;
import com.taekwondo.tournament.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    
    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Transactional
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    @Transactional
    public Match updateMatch(Long id, Match matchDetails) {
        Match match = matchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        
        match.setMatchId(matchDetails.getMatchId());
        match.setTournament(matchDetails.getTournament());
        match.setParticipant1(matchDetails.getParticipant1());
        match.setParticipant2(matchDetails.getParticipant2());
        match.setRound(matchDetails.getRound());
        match.setMatNumber(matchDetails.getMatNumber());
        match.setMatchOrder(matchDetails.getMatchOrder());
        match.setScheduledTime(matchDetails.getScheduledTime());
        match.setWinner(matchDetails.getWinner());
        
        return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(Long id) {
        Match match = matchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        matchRepository.delete(match);
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchesByTournament(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchesByTournamentAndRound(Long tournamentId, String round) {
        return matchRepository.findByTournamentIdAndRound(tournamentId, round);
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchesByTournamentAndMat(Long tournamentId, Integer matNumber) {
        return matchRepository.findByTournamentIdAndMatNumber(tournamentId, matNumber);
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchesByParticipant(Long participantId) {
        return matchRepository.findByParticipant1IdOrParticipant2Id(participantId, participantId);
    }
} 