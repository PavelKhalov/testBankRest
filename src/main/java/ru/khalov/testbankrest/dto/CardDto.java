package ru.khalov.testbankrest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CardDto (
        String number,
        UUID ownerId,
        BigDecimal balance
){
}
