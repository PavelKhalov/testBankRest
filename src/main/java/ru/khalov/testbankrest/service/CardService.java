package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khalov.testbankrest.dto.CardDto;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final EncryptionService encryptionService;
    private final UserRepository userRepository;

    public String createCard(CardDto cardDto){
        log.info("Called method: createCard in CardService");
        User owner =
    }
}
