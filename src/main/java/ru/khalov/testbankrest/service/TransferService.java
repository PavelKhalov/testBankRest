package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khalov.testbankrest.repository.CardRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final CardRepository cardRepository;

}
