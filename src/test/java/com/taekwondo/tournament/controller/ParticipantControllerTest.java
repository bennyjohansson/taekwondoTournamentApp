package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Club;
import com.taekwondo.tournament.model.Participant;
import com.taekwondo.tournament.model.Gender;
import com.taekwondo.tournament.model.SkillLevel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ParticipantControllerTest extends BaseIntegrationTest {

    @Test
    void getAllParticipants_ShouldReturnEmptyList_WhenNoParticipantsExist() {
        ResponseEntity<List<Participant>> response = restTemplate.exchange(
            createURLWithPort("/api/participants"),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Participant>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createParticipant_ShouldReturnCreatedParticipant() {
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        ResponseEntity<Participant> response = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant,
            Participant.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        assertEquals(Gender.MALE, response.getBody().getGender());
        assertEquals(SkillLevel.BLACK_BELT, response.getBody().getSkillLevel());
    }

    @Test
    void getParticipantById_ShouldReturnParticipant_WhenParticipantExists() {
        // First create a participant
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        ResponseEntity<Participant> createResponse = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant,
            Participant.class
        );

        Long participantId = createResponse.getBody().getId();

        // Then get it by ID
        ResponseEntity<Participant> response = restTemplate.getForEntity(
            createURLWithPort("/api/participants/" + participantId),
            Participant.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void getParticipantById_ShouldReturnNotFound_WhenParticipantDoesNotExist() {
        ResponseEntity<Participant> response = restTemplate.getForEntity(
            createURLWithPort("/api/participants/999"),
            Participant.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getParticipantsByClub_ShouldReturnParticipants_WhenClubExists() {
        // First create a club
        Club club = new Club();
        club.setName("Test Club");
        club.setLocation("Test Location");

        ResponseEntity<Club> clubResponse = restTemplate.postForEntity(
            createURLWithPort("/api/clubs"),
            club,
            Club.class
        );

        Long clubId = clubResponse.getBody().getId();

        // Create a participant in that club
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);
        participant.setClub(clubResponse.getBody());

        restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant,
            Participant.class
        );

        // Get participants by club
        ResponseEntity<List<Participant>> response = restTemplate.exchange(
            createURLWithPort("/api/participants/club/" + clubId),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Participant>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void getParticipantsByGenderAndSkillLevel_ShouldReturnFilteredParticipants() {
        // Create a participant
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant,
            Participant.class
        );

        // Search by gender and skill level
        ResponseEntity<List<Participant>> response = restTemplate.exchange(
            createURLWithPort("/api/participants/search?gender=MALE&skillLevel=BLACK_BELT"),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Participant>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(Gender.MALE, response.getBody().get(0).getGender());
        assertEquals(SkillLevel.BLACK_BELT, response.getBody().get(0).getSkillLevel());
    }
} 