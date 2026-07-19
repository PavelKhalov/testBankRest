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
import ru.khalov.testbankrest.dto.request.TransferRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.service.CardService;
import ru.khalov.testbankrest.service.TransferService;
import ru.khalov.testbankrest.util.CardStatus;
import ru.khalov.testbankrest.util.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCardControllerTest {

    @Mock
    private CardService cardService;
    @Mock
    private TransferService transferService;

    @InjectMocks
    private UserCardController userCardController;

    private final User currentUser = User.builder().username("john").role(Role.USER).build();

    private CardResponse cardResponse() {
        return new CardResponse( "**** **** **** 1234", "john", "John", "Doe",
                LocalDate.now().plusYears(1), CardStatus.ACTIVE, BigDecimal.TEN);
    }

    @Test
    void getAllCardsTest() {
        Pageable pageable = PageRequest.of(0, 5);
        PageResponse<CardResponse> page = new PageResponse<>(List.of(cardResponse()), 0, 5, 1, 1, true);

        when(cardService.getCards("john", pageable)).thenReturn(page);

        ResponseEntity<PageResponse<CardResponse>> response = userCardController.getAllCards(currentUser, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void getCardByIdTest() {
        CardResponse expected = cardResponse();
        when(cardService.getCardById("john", 1L)).thenReturn(expected);

        ResponseEntity<CardResponse> response = userCardController.getCardById(currentUser, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getCardBalanceTest() {
        when(cardService.getCardBalance("john", 1L)).thenReturn(BigDecimal.TEN);

        ResponseEntity<BigDecimal> response = userCardController.getCardBalance(currentUser, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, response.getBody());
    }

    @Test
    void requestBlockTest() {
        when(cardService.requestBlockCard("john", 1L)).thenReturn("request for blocking card send successfully");

        ResponseEntity<String> response = userCardController.requestBlock(currentUser, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("request for blocking card send successfully", response.getBody());
    }

    @Test
    void makeTransferTest() {
        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.ONE, null);
        when(transferService.transfer("john", request)).thenReturn("Transfer ended successfully");

        ResponseEntity<String> response = userCardController.makeTransfer(currentUser, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer ended successfully", response.getBody());
    }
}
