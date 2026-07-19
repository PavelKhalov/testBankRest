package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.dto.request.TransferRequest;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.exception.CardNotActiveException;
import ru.khalov.testbankrest.exception.CardNotFoundException;
import ru.khalov.testbankrest.exception.CardOwnershipException;
import ru.khalov.testbankrest.exception.SameCardTransferException;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.util.CardStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final CardRepository cardRepository;

    @Transactional()
    public String transfer(TransferRequest request){
        Long id1 = request.fromCardId();
        Long id2 = request.toCardId();

        if(id1.equals(id2))
            throw new SameCardTransferException("You cannot transfer money to the same card");


        Card fromCard, toCard;

        if(id1 < id2){
            fromCard = lockCard(id1);
            toCard = lockCard(id2);
        }else{
            toCard = lockCard(id2);
            fromCard = lockCard(id1);
        }

        if(!fromCard.getOwner().getUsername().equals(toCard.getOwner()))
            throw new CardOwnershipException("It's not your card");


        if(fromCard.getOwner().equals(toCard.getOwner()))
            throw new CardOwnershipException("different cardholders");


        if(fromCard.getStatus() != CardStatus.ACTIVE || toCard.getStatus() != CardStatus.ACTIVE)
            throw new CardNotActiveException("Card is not active");

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));
        return "Transfer ended successfully";
    }


    private Card lockCard(Long cardId) {
        return cardRepository.findByIdForUpdate(cardId).orElseThrow(() ->
                new CardNotFoundException("Card with id: " + cardId + " not found"));
    }
}
