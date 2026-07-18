package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferRequest (
        @NotNull(message = "Id sender card must be provided")
        Long fromCardId,

        @NotNull(message = "Id recipient card must be provided")
        Long toCardId,

        @NotNull(message = "Money must be provided")
        @DecimalMin(value = "0.1", message = "Min value send money is: 0.1")
        @Digits(integer = 5, fraction = 2, message = "Max value for send money is: 99_999.99")
        BigDecimal amount,

        Instant when
){
}
