package ru.khalov.testbankrest.dto;

import java.util.List;

public record UserDto (
        String username,
        String password,
        String name,
        String surname,
        String email,
        List<Long> cardIds
){
}
