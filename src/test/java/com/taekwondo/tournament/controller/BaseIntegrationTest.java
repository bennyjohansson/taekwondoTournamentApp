package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    
    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected MatchRepository matchRepository;

    @Autowired
    protected TournamentRepository tournamentRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected ClubRepository clubRepository;

    protected String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @AfterEach
    void cleanup() {
        matchRepository.deleteAll();
        tournamentRepository.deleteAll();
        participantRepository.deleteAll();
        clubRepository.deleteAll();
    }
} 