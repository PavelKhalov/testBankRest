package ru.khalov.testbankrest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OperationDto(
        String fromCard,
        String toCard,
        BigDecimal amount,
        Instant when
) {
}
