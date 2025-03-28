package com.taekwondo.tournament.repository;

import com.taekwondo.tournament.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // Basic CRUD operations are automatically provided by JpaRepository
} 