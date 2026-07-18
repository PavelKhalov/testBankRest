package ru.khalov.testbankrest.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CardResponse(
        String cardNumber,
        UUID ownerId,
        String ownerName,
        String ownerSurname,
        BigDecimal balance
) {
}
