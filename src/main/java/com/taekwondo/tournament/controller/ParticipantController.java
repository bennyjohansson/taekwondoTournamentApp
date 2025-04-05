package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Participant;
import com.taekwondo.tournament.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantController.class);
    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        logger.info("Test endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint working");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        logger.info("Received request to get all participants");
        try {
            List<Participant> participants = participantService.getAllParticipants();
            logger.info("Successfully retrieved {} participants", participants.size());
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            logger.error("Error retrieving participants: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        logger.info("Received request to get participant with id: {}", id);
        try {
            return participantService.getParticipantById(id)
                .map(participant -> {
                    logger.info("Successfully retrieved participant with id: {}", id);
                    return ResponseEntity.ok(participant);
                })
                .orElseGet(() -> {
                    logger.warn("Participant not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        } catch (Exception e) {
            logger.error("Error retrieving participant with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.createParticipant(participant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participantDetails) {
        return ResponseEntity.ok(participantService.updateParticipant(id, participantDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Participant>> getParticipantsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(participantService.getParticipantsByClub(clubId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Participant>> getParticipantsByGenderAndSkillLevel(
            @RequestParam String gender,
            @RequestParam String skillLevel) {
        return ResponseEntity.ok(participantService.getParticipantsByGenderAndSkillLevel(gender, skillLevel));
    }
} 