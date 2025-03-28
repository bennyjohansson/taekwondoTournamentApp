package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Participant;
import com.taekwondo.tournament.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
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