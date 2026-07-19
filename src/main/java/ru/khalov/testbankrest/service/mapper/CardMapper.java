package ru.khalov.testbankrest.service.mapper;

import org.springframework.stereotype.Component;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.util.CardNumberMasker;

@Component
public class CardMapper {

    public CardResponse toResponse(Card card){
        return new CardResponse(
                CardNumberMasker.mask(card.getLastFourDigits()),
                card.getOwner().getUsername(),
                card.getOwner().getName(),
                card.getOwner().getSurname(),
                card.getExpirationDate(),
                card.getStatus(),
                card.getBalance()
        );
    }

}
