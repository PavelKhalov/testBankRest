package ru.khalov.testbankrest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khalov.testbankrest.dto.request.CreateCardRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.service.CardService;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private AdminCardController adminCardController;

    private CardResponse cardResponse() {
        return new CardResponse( "**** **** **** 1234", "john", "John", "Doe",
                LocalDate.now().plusYears(1), CardStatus.ACTIVE, BigDecimal.ZERO);
    }

    @Test
    void createCardTest() {
        CreateCardRequest request = new CreateCardRequest("john", "1234567812341234",
                LocalDate.now().plusYears(1), BigDecimal.ZERO);
        CardResponse expected = cardResponse();

        when(cardService.createCard(request)).thenReturn(expected);

        ResponseEntity<CardResponse> response = adminCardController.createCard(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getAllCardsTest() {
        Pageable pageable = PageRequest.of(0, 5);
        PageResponse<CardResponse> page = new PageResponse<>(List.of(cardResponse()), 0, 5, 1, 1, true);

        when(cardService.getAllCards(null, pageable)).thenReturn(page);

        ResponseEntity<PageResponse<CardResponse>> response = adminCardController.getAllCards(null, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void blockCrdTest() {
        CardResponse expected = cardResponse();
        when(cardService.setStatus(1L, CardStatus.BLOCKED)).thenReturn(expected);

        ResponseEntity<CardResponse> response = adminCardController.blockCrd(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void activateCardTest() {
        CardResponse expected = cardResponse();
        when(cardService.setStatus(1L, CardStatus.ACTIVE)).thenReturn(expected);

        ResponseEntity<CardResponse> response = adminCardController.activateCard(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void deleteCardTest() {
        when(cardService.deleteCard(1L)).thenReturn("Card delete successfully!");

        ResponseEntity<String> response = adminCardController.deleteCard(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Card delete successfully!", response.getBody());
    }
}
