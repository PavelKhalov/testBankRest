package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank(message = "Username must be provided")
        String username,

        @NotBlank(message = "Password must be provided")
        String password
){
}
