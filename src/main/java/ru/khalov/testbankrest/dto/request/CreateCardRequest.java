package ru.khalov.testbankrest.dto.request;

import jakarta.validation.constraints.*;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest (
        @NotBlank(message = "username owner card's must be provided")
        String ownerUsername,

        @NotNull(message = "Card number must be provided")
        @Pattern(regexp = "\\d{16}", message = "Номер карты должен состоять ровно из 16 цифр")
        String cardNumber,

        @NotNull(message = "The validity period of the card must be specified")
        @Future(message = "The card expiration date should be in the future")
        LocalDate expirationDate,

        @NotNull(message = "Start balance must be provided")
        @DecimalMin(value = "0.00", message = "Balance should not be lower than 0")
        @Digits(integer = 9, fraction = 2, message = "Uncorrected sum format")
        BigDecimal startedBalance
) {
}
