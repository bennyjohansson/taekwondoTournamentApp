package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

class MatchControllerTest extends BaseIntegrationTest {

    @Test
    void getAllMatches_ShouldReturnEmptyList_WhenNoMatchesExist() {
        ResponseEntity<List<Match>> response = restTemplate.exchange(
            createURLWithPort("/api/matches"),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Match>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createMatch_ShouldReturnCreatedMatch() {
        // First create a tournament
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now());
        tournament.setLocation("Test Location");
        tournament.setNumberOfMats(2);

        ResponseEntity<Tournament> tournamentResponse = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        // Create participants
        Participant participant1 = new Participant();
        participant1.setName("John Doe");
        participant1.setAge(25);
        participant1.setGender(Gender.MALE);
        participant1.setSkillLevel(SkillLevel.BLACK_BELT);

        Participant participant2 = new Participant();
        participant2.setName("Jane Doe");
        participant2.setAge(25);
        participant2.setGender(Gender.FEMALE);
        participant2.setSkillLevel(SkillLevel.BLACK_BELT);

        ResponseEntity<Participant> participant1Response = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant1,
            Participant.class
        );

        ResponseEntity<Participant> participant2Response = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant2,
            Participant.class
        );

        // Create a match
        Match match = new Match();
        match.setTournament(tournamentResponse.getBody());
        match.setParticipant1(participant1Response.getBody());
        match.setParticipant2(participant2Response.getBody());
        match.setRound(Match.Round.QUARTER_FINAL);
        match.setMatNumber(1);
        match.setMatchOrder(1);

        ResponseEntity<Match> response = restTemplate.postForEntity(
            createURLWithPort("/api/matches"),
            match,
            Match.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Match.Round.QUARTER_FINAL, response.getBody().getRound());
        assertEquals(1, response.getBody().getMatNumber());
    }

    @Test
    void getMatchById_ShouldReturnMatch_WhenMatchExists() {
        // First create necessary entities and a match (similar to createMatch test)
        // Then test getting it by ID
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now());
        tournament.setLocation("Test Location");
        tournament.setNumberOfMats(2);

        ResponseEntity<Tournament> tournamentResponse = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        Participant participant1 = new Participant();
        participant1.setName("John Doe");
        participant1.setAge(25);
        participant1.setGender(Gender.MALE);
        participant1.setSkillLevel(SkillLevel.BLACK_BELT);

        ResponseEntity<Participant> participant1Response = restTemplate.postForEntity(
            createURLWithPort("/api/participants"),
            participant1,
            Participant.class
        );

        Match match = new Match();
        match.setTournament(tournamentResponse.getBody());
        match.setParticipant1(participant1Response.getBody());
        match.setRound(Match.Round.QUARTER_FINAL);
        match.setMatNumber(1);
        match.setMatchOrder(1);

        ResponseEntity<Match> createResponse = restTemplate.postForEntity(
            createURLWithPort("/api/matches"),
            match,
            Match.class
        );

        Long matchId = createResponse.getBody().getId();

        // Get match by ID
        ResponseEntity<Match> response = restTemplate.getForEntity(
            createURLWithPort("/api/matches/" + matchId),
            Match.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Match.Round.QUARTER_FINAL, response.getBody().getRound());
    }

    @Test
    void getMatchesByTournament_ShouldReturnMatches_WhenMatchesExist() {
        // Create tournament and match
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now());
        tournament.setLocation("Test Location");
        tournament.setNumberOfMats(2);

        ResponseEntity<Tournament> tournamentResponse = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        Match match = new Match();
        match.setTournament(tournamentResponse.getBody());
        match.setRound(Match.Round.QUARTER_FINAL);
        match.setMatNumber(1);
        match.setMatchOrder(1);

        restTemplate.postForEntity(
            createURLWithPort("/api/matches"),
            match,
            Match.class
        );

        // Get matches by tournament
        ResponseEntity<List<Match>> response = restTemplate.exchange(
            createURLWithPort("/api/matches/tournament/" + tournamentResponse.getBody().getId()),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Match>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(Match.Round.QUARTER_FINAL, response.getBody().get(0).getRound());
    }

    @Test
    void getMatchesByTournamentAndRound_ShouldReturnMatches_WhenMatchesExist() {
        // Create tournament and match
        Tournament tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDate(LocalDate.now());
        tournament.setLocation("Test Location");
        tournament.setNumberOfMats(2);

        ResponseEntity<Tournament> tournamentResponse = restTemplate.postForEntity(
            createURLWithPort("/api/tournaments"),
            tournament,
            Tournament.class
        );

        Match match = new Match();
        match.setTournament(tournamentResponse.getBody());
        match.setRound(Match.Round.QUARTER_FINAL);
        match.setMatNumber(1);
        match.setMatchOrder(1);

        restTemplate.postForEntity(
            createURLWithPort("/api/matches"),
            match,
            Match.class
        );

        // Get matches by tournament and round
        ResponseEntity<List<Match>> response = restTemplate.exchange(
            createURLWithPort("/api/matches/tournament/" + 
                tournamentResponse.getBody().getId() + "/round/" + Match.Round.QUARTER_FINAL),
            org.springframework.http.HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Match>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(Match.Round.QUARTER_FINAL, response.getBody().get(0).getRound());
    }
} 