package com.salespilot.api.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {

    private BCryptPasswordHasher hasher;

    @BeforeEach
    void setUp() {
        hasher = new BCryptPasswordHasher();
    }

    @Test
    void shouldHashAndMatchCorrectPassword() {
        String hash = hasher.hash("myPassword");

        assertTrue(hasher.matches("myPassword", hash));
    }

    @Test
    void shouldReturnFalseForWrongPassword() {
        String hash = hasher.hash("myPassword");

        assertFalse(hasher.matches("wrongPassword", hash));
    }

    @Test
    void shouldProduceDifferentHashesForSameInput() {
        String hash1 = hasher.hash("same");
        String hash2 = hasher.hash("same");

        assertNotEquals(hash1, hash2);
    }
}
