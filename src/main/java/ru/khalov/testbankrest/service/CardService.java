package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.dto.request.CreateCardRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.CardNotFoundException;
import ru.khalov.testbankrest.exception.CardOwnershipException;
import ru.khalov.testbankrest.exception.UserNotFoundException;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.service.mapper.CardMapper;
import ru.khalov.testbankrest.util.CardNumberEncryptor;
import ru.khalov.testbankrest.util.CardStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptor encryptor;
    private final CardMapper cardMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CardResponse createCard(CreateCardRequest request){
        User owner = userRepository.findByUsername(request.ownerUsername()).orElseThrow(() ->
                new UserNotFoundException("User with username: " + request.ownerUsername() + " not found"));

        String lastFour = request.cardNumber().substring(request.cardNumber().length() - 4);

        Card card = Card.builder()
                .number(encryptor.encrypt(request.cardNumber()))
                .lastFourDigits(lastFour)
                .balance(request.startedBalance())
                .owner(owner)
                .expirationDate(request.expirationDate())
                .status(CardStatus.ACTIVE)
                .build();

        cardRepository.save(card);

        return cardMapper.toResponse(card);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<CardResponse> getAllCards(String ownerUsername, Pageable pageable){
        List<Card> cards = (ownerUsername != null)
                ? cardRepository.findAllByOwnerUsername(ownerUsername)
                : cardRepository.findAll();

        return PageResponse.from(paginate(cards, pageable).map(cardMapper::toResponse));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CardResponse setStatus(Long cardId, CardStatus status){
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new CardNotFoundException("Card with id: " + cardId + " not found"));

        card.setStatus(status);
        return cardMapper.toResponse(card);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String deleteCard(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new CardNotFoundException("Card with id: " + cardId + " not found"));

        cardRepository.delete(card);
        return "Card delete successfully!";
    }


    public PageResponse<CardResponse> getCards(String ownerUsername, Pageable pageable){
        List<Card> cards = cardRepository.findAllByOwnerUsername(ownerUsername);
        return PageResponse.from(paginate(cards, pageable).map(cardMapper::toResponse));
    }

    public CardResponse getCardById(String ownerUsername, Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new CardNotFoundException("Card with id: " + cardId + " not found"));

        if(!card.getOwner().getUsername().equals(ownerUsername)){
            throw new CardOwnershipException("It's not your card");
        }

        return cardMapper.toResponse(card);
    }

    public BigDecimal getCardBalance(String ownerUsername, Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new CardNotFoundException("Card with id: " + cardId + " not found"));

        if(!card.getOwner().getUsername().equals(ownerUsername)){
            throw new CardOwnershipException("It's not your card");
        }

        return card.getBalance();
    }

    @Transactional
    public String requestBlockCard(String ownerUsername, Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new CardNotFoundException("Card with id: " + cardId + " not found"));

        if(!card.getOwner().getUsername().equals(ownerUsername)){
            throw new CardOwnershipException("It's not your card");
        }

        card.setStatus(CardStatus.BLOCKED);
        return "request for blocking card send successfully";
    }

    private Page<Card> paginate(List<Card> cards, Pageable pageable) {
        int start = (int) pageable.getOffset();
        if (start >= cards.size()) {
            return new PageImpl<>(List.of(), pageable, cards.size());
        }
        int end = Math.min(start + pageable.getPageSize(), cards.size());
        return new PageImpl<>(cards.subList(start, end), pageable, cards.size());
    }
}
