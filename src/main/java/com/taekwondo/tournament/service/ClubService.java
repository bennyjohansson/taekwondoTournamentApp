package com.taekwondo.tournament.service;

import com.taekwondo.tournament.model.Club;
import com.taekwondo.tournament.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    
    private final ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Transactional(readOnly = true)
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Club> getClubById(Long id) {
        return clubRepository.findById(id);
    }

    @Transactional
    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    @Transactional
    public Club updateClub(Long id, Club clubDetails) {
        Club club = clubRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Club not found with id: " + id));
        
        club.setName(clubDetails.getName());
        club.setLocation(clubDetails.getLocation());
        
        return clubRepository.save(club);
    }

    @Transactional
    public void deleteClub(Long id) {
        Club club = clubRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Club not found with id: " + id));
        clubRepository.delete(club);
    }
} 