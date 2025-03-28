package com.taekwondo.tournament.service;

import com.taekwondo.tournament.model.Participant;
import com.taekwondo.tournament.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    @Transactional
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Transactional
    public Participant updateParticipant(Long id, Participant participantDetails) {
        Participant participant = participantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));
        
        participant.setName(participantDetails.getName());
        participant.setAge(participantDetails.getAge());
        participant.setGender(participantDetails.getGender());
        participant.setSkillLevel(participantDetails.getSkillLevel());
        participant.setClub(participantDetails.getClub());
        
        return participantRepository.save(participant);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        Participant participant = participantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));
        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByClub(Long clubId) {
        return participantRepository.findByClubId(clubId);
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByGenderAndSkillLevel(String gender, String skillLevel) {
        return participantRepository.findByGenderAndSkillLevel(gender, skillLevel);
    }
} 