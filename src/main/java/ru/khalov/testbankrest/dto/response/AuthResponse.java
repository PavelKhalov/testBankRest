package ru.khalov.testbankrest.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        String username,
        String role
) {
}
