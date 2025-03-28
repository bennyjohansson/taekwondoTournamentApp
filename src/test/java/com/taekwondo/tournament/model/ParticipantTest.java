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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Participant entity.
 * Tests validation rules and data integrity for participant information.
 */
@DisplayName("Participant Entity Tests")
class ParticipantTest {
    private static final Logger logger = LoggerFactory.getLogger(ParticipantTest.class);
    private Validator validator;
    private Participant participant;

    /**
     * Set up test environment before each test.
     * Creates a new validator instance and a fresh participant object.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        participant = new Participant();
    }

    /**
     * Test scenario: Valid participant data
     * Expected: No validation errors when all required fields are properly set
     * - Name is not blank
     * - Age is positive
     * - Gender is specified
     * - Skill level is specified
     */
    @Test
    @DisplayName("Should validate participant with all required fields")
    void testValidParticipantData_ShouldNotHaveValidationErrors() {
        // Given
        logger.info("Setting up valid participant data");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Participant.Gender.MALE);
        participant.setSkillLevel(Participant.SkillLevel.ADVANCED);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertTrue(violations.isEmpty(), "A valid participant should not have any validation errors");
        logger.info("Test passed: Participant data is valid");
    }

    /**
     * Test scenario: Blank participant name
     * Expected: Validation error with message "Name is required"
     * - Name is blank
     * - Other fields are valid
     */
    @Test
    @DisplayName("Should reject participant with blank name")
    void testBlankParticipantName_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with blank name");
        participant.setName("");
        participant.setAge(25);
        participant.setGender(Participant.Gender.MALE);
        participant.setSkillLevel(Participant.SkillLevel.ADVANCED);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Name is required", violations.iterator().next().getMessage(), 
            "Validation error should indicate that name is required");
        logger.info("Test passed: Blank name validation error detected");
    }

    /**
     * Test scenario: Negative participant age
     * Expected: Validation error with message "Age must be positive"
     * - Age is negative (-1)
     * - Other fields are valid
     */
    @Test
    @DisplayName("Should reject participant with negative age")
    void testNegativeParticipantAge_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with negative age");
        participant.setName("John Doe");
        participant.setAge(-1);
        participant.setGender(Participant.Gender.MALE);
        participant.setSkillLevel(Participant.SkillLevel.ADVANCED);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Age must be positive", violations.iterator().next().getMessage(),
            "Validation error should indicate that age must be positive");
        logger.info("Test passed: Negative age validation error detected");
    }

    /**
     * Test scenario: Missing participant gender
     * Expected: Validation error with message "Gender is required"
     * - Gender is null
     * - Other fields are valid
     */
    @Test
    @DisplayName("Should reject participant with missing gender")
    void testMissingParticipantGender_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with missing gender");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setSkillLevel(Participant.SkillLevel.ADVANCED);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Gender is required", violations.iterator().next().getMessage(),
            "Validation error should indicate that gender is required");
        logger.info("Test passed: Missing gender validation error detected");
    }

    /**
     * Test scenario: Missing participant skill level
     * Expected: Validation error with message "Skill level is required"
     * - Skill level is null
     * - Other fields are valid
     */
    @Test
    @DisplayName("Should reject participant with missing skill level")
    void testMissingParticipantSkillLevel_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with missing skill level");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Participant.Gender.MALE);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Skill level is required", violations.iterator().next().getMessage(),
            "Validation error should indicate that skill level is required");
        logger.info("Test passed: Missing skill level validation error detected");
    }
} 