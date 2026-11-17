package com.example.tourist.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService = new JwtService(
            "test-secret-key-that-is-at-least-32-bytes-long", 3600);

    @Test
    void generatedTokenIsValidAndContainsUsername() {
        String token = jwtService.generateToken("hoteladmin");
        assertTrue(jwtService.isTokenValid(token));
        assertEquals("hoteladmin", jwtService.extractUsername(token));
    }

    @Test
    void malformedTokenIsInvalid() {
        assertFalse(jwtService.isTokenValid("not.a.valid.token"));
    }
}
