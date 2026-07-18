package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.NotNull;
import ru.khalov.testbankrest.util.Role;

public record UpdateUserRoleRequest(
        @NotNull(message = "Role must be provided")
        Role role
) {
}
