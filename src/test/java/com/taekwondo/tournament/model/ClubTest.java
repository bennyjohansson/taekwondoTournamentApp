package com.taekwondo.tournament.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * Test class for the Club entity.
 * Tests validation rules and relationships between Club and Participant entities.
 */
@DisplayName("Club Entity Tests")
class ClubTest {
    private static final Logger logger = LoggerFactory.getLogger(ClubTest.class);
    private Validator validator;
    private Club club;

    /**
     * Set up test environment before each test.
     * Creates a new validator instance and a fresh club object.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        club = new Club();
    }

    /**
     * Test scenario: Valid club data
     * Expected: No validation errors when all required fields are properly set
     * - Name is not blank
     * - Location is specified (optional)
     */
    @Test
    @DisplayName("Should validate club with all required fields")
    void testValidClubData_ShouldNotHaveValidationErrors() {
        // Given
        logger.info("Setting up valid club data");
        club.setName("TKD Club");
        club.setLocation("Stockholm");

        // When
        logger.info("Validating club data");
        Set<ConstraintViolation<Club>> violations = validator.validate(club);

        // Then
        logger.info("Checking validation results");
        assertTrue(violations.isEmpty(), "A valid club should not have any validation errors");
        logger.info("Test passed: Club data is valid");
    }

    /**
     * Test scenario: Blank club name
     * Expected: Validation error with message "Club name is required"
     * - Name is blank
     * - Location is valid
     */
    @Test
    @DisplayName("Should reject club with blank name")
    void testBlankClubName_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up club with blank name");
        club.setName("");
        club.setLocation("Stockholm");

        // When
        logger.info("Validating club data");
        Set<ConstraintViolation<Club>> violations = validator.validate(club);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Club name is required", violations.iterator().next().getMessage(),
            "Validation error should indicate that club name is required");
        logger.info("Test passed: Blank name validation error detected");
    }

    /**
     * Test scenario: Missing club location
     * Expected: No validation errors when location is null
     * - Name is valid
     * - Location is null (optional field)
     */
    @Test
    @DisplayName("Should accept club without location")
    void testMissingClubLocation_ShouldNotHaveValidationError() {
        // Given
        logger.info("Setting up club without location");
        club.setName("TKD Club");

        // When
        logger.info("Validating club data");
        Set<ConstraintViolation<Club>> violations = validator.validate(club);

        // Then
        logger.info("Checking validation results");
        assertTrue(violations.isEmpty(), "A club without location should not have validation errors");
        logger.info("Test passed: Club without location is valid");
    }

    /**
     * Test scenario: Club-Participant relationship
     * Expected: Proper bidirectional relationship between Club and Participant
     * - Club has one participant in its list
     * - Participant has the correct club reference
     * - All participant data is valid
     */
    @Test
    @DisplayName("Should establish proper club-participant relationship")
    void testClubParticipantRelationship_ShouldBeProperlyEstablished() {
        // Given
        logger.info("Setting up club and participant");
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.Male);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        club.setName("TKD Club");

        // When
        logger.info("Establishing club-participant relationship");
        club.getParticipants().add(participant);
        participant.setClub(club);

        // Then
        logger.info("Verifying relationship");
        assertEquals(1, club.getParticipants().size(), 
            "Club should have exactly one participant");
        assertEquals(club, participant.getClub(), 
            "Participant should reference the correct club");
        logger.info("Test passed: Club-participant relationship established correctly");
    }
} 