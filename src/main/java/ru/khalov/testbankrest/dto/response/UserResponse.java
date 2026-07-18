package ru.khalov.testbankrest.dto.response;

import ru.khalov.testbankrest.util.Role;

import java.util.UUID;

public record UserResponse (
        UUID id,
        String username,
        String name,
        String surname,
        String email,
        Role role,
        boolean enabled
) {
}
