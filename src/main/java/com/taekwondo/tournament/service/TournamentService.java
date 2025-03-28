package com.taekwondo.tournament.service;

import com.taekwondo.tournament.model.Tournament;
import com.taekwondo.tournament.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Transactional
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Transactional
    public Tournament updateTournament(Long id, Tournament tournamentDetails) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + id));
        
        tournament.setName(tournamentDetails.getName());
        tournament.setDate(tournamentDetails.getDate());
        tournament.setNumberOfMats(tournamentDetails.getNumberOfMats());
        tournament.setCategories(tournamentDetails.getCategories());
        
        return tournamentRepository.save(tournament);
    }

    @Transactional
    public void deleteTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + id));
        tournamentRepository.delete(tournament);
    }

    @Transactional(readOnly = true)
    public List<Tournament> getTournamentsByDate(LocalDate date) {
        return tournamentRepository.findByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Tournament> getTournamentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return tournamentRepository.findByDateBetween(startDate, endDate);
    }
} 