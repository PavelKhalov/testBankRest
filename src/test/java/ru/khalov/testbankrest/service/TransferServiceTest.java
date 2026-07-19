package ru.khalov.testbankrest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khalov.testbankrest.dto.request.TransferRequest;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.CardOwnershipException;
import ru.khalov.testbankrest.exception.SameCardTransferException;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void transferTest() {
        TransferRequest request = new TransferRequest(1L, 1L, BigDecimal.ONE, null);

        assertThrows(SameCardTransferException.class, () -> transferService.transfer("john",request));
    }

    @Test
    void transferTest_CardOwnershipException() {
        User owner = User.builder().username("john").build();

        Card fromCard = Card.builder().id(1L).owner(owner).balance(BigDecimal.TEN).status(CardStatus.ACTIVE).build();
        Card toCard = Card.builder().id(2L).owner(owner).balance(BigDecimal.ZERO).status(CardStatus.ACTIVE).build();

        when(cardRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.ONE, null);

        assertThrows(CardOwnershipException.class, () -> transferService.transfer("john", request));
    }
}
