package ru.khalov.testbankrest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.khalov.testbankrest.dto.request.CreateCardRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.service.mapper.CardMapper;
import ru.khalov.testbankrest.util.CardNumberEncryptor;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardNumberEncryptor encryptor;
    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private User owner;
    private Card card;
    private CardResponse cardResponse;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(UUID.randomUUID())
                .username("john")
                .name("John")
                .surname("Doe")
                .build();

        card = Card.builder()
                .id(1L)
                .number("encrypted")
                .lastFourDigits("1234")
                .balance(BigDecimal.TEN)
                .owner(owner)
                .expirationDate(LocalDate.now().plusYears(1))
                .status(CardStatus.ACTIVE)
                .build();

        cardResponse = new CardResponse( "**** **** **** 1234", "john", "John", "Doe",
                card.getExpirationDate(), CardStatus.ACTIVE, BigDecimal.TEN);
    }

    @Test
    void createCardTest() {
        CreateCardRequest request = new CreateCardRequest("john", "1234567812341234",
                LocalDate.now().plusYears(1), BigDecimal.TEN);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(owner));
        when(encryptor.encrypt("1234567812341234")).thenReturn("encrypted");
        when(cardMapper.toResponse(any(Card.class))).thenReturn(cardResponse);

        CardResponse response = cardService.createCard(request);

        assertEquals(cardResponse, response);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void getAllCardsTest() {
        Pageable pageable = PageRequest.of(0, 5);
        when(cardRepository.findAll()).thenReturn(List.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        PageResponse<CardResponse> result = cardService.getAllCards(null, pageable);

        assertEquals(1, result.content().size());
        assertEquals(cardResponse, result.content().get(0));
    }

    @Test
    void setStatusTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse response = cardService.setStatus(1L, CardStatus.BLOCKED);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertEquals(cardResponse, response);
    }

    @Test
    void deleteCardTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        String result = cardService.deleteCard(1L);

        verify(cardRepository).delete(card);
        assertEquals("Card delete successfully!", result);
    }

    @Test
    void getCardsTest() {
        Pageable pageable = PageRequest.of(0, 5);
        when(cardRepository.findAllByOwnerUsername("john")).thenReturn(List.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        PageResponse<CardResponse> result = cardService.getCards("john", pageable);

        assertEquals(1, result.content().size());
        assertEquals(cardResponse, result.content().get(0));
    }

    @Test
    void getCardByIdTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse response = cardService.getCardById("john", 1L);

        assertEquals(cardResponse, response);
    }

    @Test
    void getCardBalanceTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        BigDecimal balance = cardService.getCardBalance("john", 1L);

        assertEquals(BigDecimal.TEN, balance);
    }

    @Test
    void requestBlockCardTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        String result = cardService.requestBlockCard("john", 1L);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertEquals("request for blocking card send successfully", result);
    }
}
