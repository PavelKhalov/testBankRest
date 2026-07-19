package ru.khalov.testbankrest.dto.response;

import ru.khalov.testbankrest.util.Role;

public record UserResponse (
        String username,
        String name,
        String surname,
        String email,
        Role role,
        boolean enabled
) {
}
