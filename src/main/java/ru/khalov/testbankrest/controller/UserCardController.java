package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.khalov.testbankrest.dto.request.TransferRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.service.CardService;
import ru.khalov.testbankrest.service.TransferService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "User card", description = "viewing your cards, requesting a lock, balance, and transfers")
public class UserCardController {

    private final CardService cardService;
    private final TransferService transferService;

    @GetMapping()
    public ResponseEntity<PageResponse<CardResponse>> getAllCards(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 5) Pageable pageable
            ){
        return ResponseEntity.ok(cardService.getCards(user.getUsername(), pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@AuthenticationPrincipal User user ,@PathVariable Long id){
        return ResponseEntity.ok(cardService.getCardById(user.getUsername(), id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@AuthenticationPrincipal User user, @PathVariable Long id){
        return ResponseEntity.ok(cardService.getCardBalance(user.getUsername(), id));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<String> requestBlock(@AuthenticationPrincipal User user, @PathVariable Long id){
        return ResponseEntity.ok(cardService.requestBlockCard(user.getUsername(), id));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> makeTransfer(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody TransferRequest transferRequest){

        return ResponseEntity.ok(transferService.transfer(transferRequest));
    }

}
