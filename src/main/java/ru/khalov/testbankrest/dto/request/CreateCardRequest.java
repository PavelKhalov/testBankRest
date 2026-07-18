package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateCardRequest (
        @NotNull(message = "Id owner card's must be provided")
        Long ownerId,

        @NotNull(message = "Card number must be provided")
        @Pattern(regexp = "\\d{16}", message = "Номер карты должен состоять ровно из 16 цифр")
        String cardNumber,


        @NotNull(message = "Start balance must be provided")
        @DecimalMin(value = "0.00", message = "Balance should not be lower than 0")
        @Digits(integer = 9, fraction = 2, message = "Uncorrected sum format")
        BigDecimal initialBalance
) {
}
