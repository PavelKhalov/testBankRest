package ru.khalov.testbankrest.dto.response;

import org.springframework.cglib.core.Local;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardResponse(
        UUID ownerId,
        String maskCardNumber,
        String ownerUsername,
        String ownerName,
        String ownerSurname,
        LocalDate expirationDate,
        CardStatus status,
        BigDecimal balance
) {
}
