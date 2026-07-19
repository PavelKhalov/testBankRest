package ru.khalov.testbankrest.dto.response;

import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardResponse(
        String maskCardNumber,
        String ownerUsername,
        String ownerName,
        String ownerSurname,
        LocalDate expirationDate,
        CardStatus status,
        BigDecimal balance
) {
}
