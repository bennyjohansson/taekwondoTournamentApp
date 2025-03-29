package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.Tournament;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

class TournamentControllerTest extends BaseIntegrationTest {

    @Test
    void getAllTournaments_ShouldReturnEmptyList_WhenNoTournamentsExist() {
        ResponseEntity<List<Tournament>> response = restTemplate.exchange(
            createURLWithPort("/api/tournaments"),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Tournament>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createTournament_ShouldReturnCreatedTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now().plusDays(7));
        tournament.setLocation("Test Location");

        ResponseEntity<Tournament> response = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Tournament", response.getBody().getName());
        assertEquals("Test Location", response.getBody().getLocation());
    }

    @Test
    void getTournamentById_ShouldReturnTournament_WhenTournamentExists() {
        // First create a tournament
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now().plusDays(7));
        tournament.setLocation("Test Location");

        ResponseEntity<Tournament> createResponse = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        Long tournamentId = createResponse.getBody().getId();

        // Then get it by ID
        ResponseEntity<Tournament> response = restTemplate.getForEntity(
            createURLWithPort("/api/tournaments/" + tournamentId),
            Tournament.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Tournament", response.getBody().getName());
    }

    @Test
    void getTournamentById_ShouldReturnNotFound_WhenTournamentDoesNotExist() {
        ResponseEntity<Tournament> response = restTemplate.getForEntity(
            createURLWithPort("/api/tournaments/999"),
            Tournament.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTournamentsByDate_ShouldReturnTournaments_WhenTournamentsExistOnDate() {
        // Create a tournament for today
        Tournament tournament = new Tournament();
        tournament.setName("Today's Tournament");
        tournament.setDate(LocalDate.now());
        tournament.setLocation("Test Location");

        restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        // Search by today's date
        ResponseEntity<List<Tournament>> response = restTemplate.exchange(
            createURLWithPort("/api/tournaments/date/" + LocalDate.now()),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Tournament>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Today's Tournament", response.getBody().get(0).getName());
    }

    @Test
    void getTournamentsByDateRange_ShouldReturnTournaments_WhenTournamentsExistInRange() {
        // Create a tournament for tomorrow
        Tournament tournament = new Tournament();
        tournament.setName("Tomorrow's Tournament");
        tournament.setDate(LocalDate.now().plusDays(1));
        tournament.setLocation("Test Location");

        restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        // Search by date range
        String url = String.format("/api/tournaments/date-range?startDate=%s&endDate=%s",
            LocalDate.now(),
            LocalDate.now().plusDays(2));

        ResponseEntity<List<Tournament>> response = restTemplate.exchange(
            createURLWithPort(url),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Tournament>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Tomorrow's Tournament", response.getBody().get(0).getName());
    }
} 