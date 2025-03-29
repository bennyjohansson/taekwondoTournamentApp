package com.taekwondo.tournament.repository;

import com.taekwondo.tournament.model.Participant;
import com.taekwondo.tournament.model.Gender;
import com.taekwondo.tournament.model.SkillLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByClubId(Long clubId);
    List<Participant> findByGenderAndSkillLevel(Gender gender, SkillLevel skillLevel);
} 