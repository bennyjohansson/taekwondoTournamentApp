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
 * Test class for the Participant entity.
 * Tests validation rules and data integrity for participant information.
 * This includes validation of:
 * - Name (required, length constraints)
 * - Age (minimum and maximum values)
 * - Gender (required)
 * - Skill Level (required)
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
     */
    @Test
    @DisplayName("Should validate participant with all required fields")
    void testValidParticipantData_ShouldNotHaveValidationErrors() {
        // Given
        logger.info("Setting up valid participant data");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

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
     * Expected: Validation errors for blank name and length constraints
     */
    @Test
    @DisplayName("Should reject participant with blank name")
    void testBlankParticipantName_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with blank name");
        participant.setName("");
        participant.setAge(25);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(2, violations.size(), "Should have two validation errors");
        assertTrue(
            violations.stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Name is required")),
            "Should have 'Name is required' validation message"
        );
        assertTrue(
            violations.stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("Name must be between 2 and 100 characters")),
            "Should have length validation message"
        );
        logger.info("Test passed: Blank name validation errors detected");
    }

    /**
     * Test scenario: Negative participant age
     * Expected: Validation error for minimum age requirement
     */
    @Test
    @DisplayName("Should reject participant with negative age")
    void testNegativeParticipantAge_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with negative age");
        participant.setName("John Doe");
        participant.setAge(-1);
        participant.setGender(Gender.MALE);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

        // When
        logger.info("Validating participant data");
        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        // Then
        logger.info("Checking validation results");
        assertEquals(1, violations.size(), "Should have exactly one validation error");
        assertEquals("Age must be at least 4 years", violations.iterator().next().getMessage(),
            "Validation error should indicate that age must be at least 4 years");
        logger.info("Test passed: Negative age validation error detected");
    }

    /**
     * Test scenario: Missing participant gender
     * Expected: Validation error for required gender field
     */
    @Test
    @DisplayName("Should reject participant with missing gender")
    void testMissingParticipantGender_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with missing gender");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setSkillLevel(SkillLevel.BLACK_BELT);

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
     * Expected: Validation error for required skill level field
     */
    @Test
    @DisplayName("Should reject participant with missing skill level")
    void testMissingParticipantSkillLevel_ShouldHaveValidationError() {
        // Given
        logger.info("Setting up participant with missing skill level");
        participant.setName("John Doe");
        participant.setAge(25);
        participant.setGender(Gender.MALE);

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