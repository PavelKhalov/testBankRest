package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Username must be provided")
        String username,

        @NotBlank(message = "Password must be provided")
        String password,

        @NotBlank(message = "Name must be provided")
        String name,

        @NotBlank(message = "Surname must be provided")
        String surname,

        @Email
        @NotBlank(message = "Email must be provided")
        String email
) {
}
