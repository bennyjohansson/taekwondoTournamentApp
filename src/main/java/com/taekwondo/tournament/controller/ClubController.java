package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Club;
import com.taekwondo.tournament.service.ClubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private static final Logger logger = LoggerFactory.getLogger(ClubController.class);
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        logger.info("Test endpoint called for clubs");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Club test endpoint working");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        logger.info("Received request to get all clubs");
        try {
            List<Club> clubs = clubService.getAllClubs();
            logger.info("Successfully retrieved {} clubs", clubs.size());
            return ResponseEntity.ok(clubs);
        } catch (Exception e) {
            logger.error("Error retrieving clubs: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        return clubService.getClubById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        return ResponseEntity.ok(clubService.createClub(club));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable Long id, @RequestBody Club clubDetails) {
        return ResponseEntity.ok(clubService.updateClub(id, clubDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.ok().build();
    }
} 