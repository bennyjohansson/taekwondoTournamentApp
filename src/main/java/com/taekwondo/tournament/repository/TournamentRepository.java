package com.taekwondo.tournament.repository;

import com.taekwondo.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByDate(LocalDate date);
    List<Tournament> findByDateBetween(LocalDate startDate, LocalDate endDate);
} 