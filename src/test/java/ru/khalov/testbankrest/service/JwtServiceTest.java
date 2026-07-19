package ru.khalov.testbankrest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // 256-bit base64 key, only used for this test
        ReflectionTestUtils.setField(jwtService, "secretKey", "R2iXSZMedRgY7LWWpJy0oG4w-6kJ51_ucc3oOSXk8PU");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);

        userDetails = new User("john", "password", List.of());
    }

    @Test
    void generateTokenTest() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(token != null && !token.isBlank());
    }

    @Test
    void extractUsernameTest() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("john", username);
    }

    @Test
    void isTokenValidTest() {
        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }
}
