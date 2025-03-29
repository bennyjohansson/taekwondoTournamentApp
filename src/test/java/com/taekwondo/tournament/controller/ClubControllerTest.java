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

class ClubControllerTest extends BaseIntegrationTest {

    @Test
    void getAllClubs_ShouldReturnEmptyList_WhenNoClubsExist() {
        ResponseEntity<List<Club>> response = restTemplate.exchange(
            createURLWithPort("/api/clubs"),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Club>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createClub_ShouldReturnCreatedClub() {
        Club club = new Club();
        club.setName("Test Club");
        club.setLocation("Test Location");

        ResponseEntity<Club> response = restTemplate.postForEntity(
            createURLWithPort("/api/clubs"),
            club,
            Club.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Club", response.getBody().getName());
        assertEquals("Test Location", response.getBody().getLocation());
    }

    @Test
    void getClubById_ShouldReturnClub_WhenClubExists() {
        // First create a club
        Club club = new Club();
        club.setName("Test Club");
        club.setLocation("Test Location");

        ResponseEntity<Club> createResponse = restTemplate.postForEntity(
            createURLWithPort("/api/clubs"),
            club,
            Club.class
        );

        Long clubId = createResponse.getBody().getId();

        // Then get it by ID
        ResponseEntity<Club> response = restTemplate.getForEntity(
            createURLWithPort("/api/clubs/" + clubId),
            Club.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Club", response.getBody().getName());
    }

    @Test
    void getClubById_ShouldReturnNotFound_WhenClubDoesNotExist() {
        ResponseEntity<Club> response = restTemplate.getForEntity(
            createURLWithPort("/api/clubs/999"),
            Club.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createClubWithParticipant_ShouldEstablishRelationship() {
        // First create a club
        Club club = new Club();
        club.setName("Test Club");
        club.setLocation("Test Location");

        ResponseEntity<Club> clubResponse = restTemplate.postForEntity(
            createURLWithPort("/api/clubs"),
            club,
            Club.class
        );

        assertEquals(HttpStatus.OK, clubResponse.getStatusCode());
        assertNotNull(clubResponse.getBody());
        Long clubId = clubResponse.getBody().getId();

        // Then create a participant in that club
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);
        participant.setClub(clubResponse.getBody());

        ResponseEntity<Participant> participantResponse = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant,
            Participant.class
        );

        assertEquals(HttpStatus.OK, participantResponse.getStatusCode());
        assertNotNull(participantResponse.getBody());
        assertEquals("John Doe", participantResponse.getBody().getName());
        assertEquals(clubId, participantResponse.getBody().getClub().getId());
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
} 