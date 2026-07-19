package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get all user cards", description = "This operation should show for user all his cards")
    @ApiResponse(responseCode = "200", description = "All cards show successfully")
    @GetMapping()
    public ResponseEntity<PageResponse<CardResponse>> getAllCards(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 5) Pageable pageable
            ){
        return ResponseEntity.ok(cardService.getCards(user.getUsername(), pageable));
    }

    @Operation(summary = "Get user card by id", description = "This operation should show for user his card by id")
    @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "This parameter is card id")
    @ApiResponse(responseCode = "200", description = "Card show successfully")
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@AuthenticationPrincipal User user ,@PathVariable Long id){
        return ResponseEntity.ok(cardService.getCardById(user.getUsername(), id));
    }

    @Operation(summary = "Get card balance", description = "This operation should show balance card by card id")
    @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "This parameter is card id")
    @ApiResponse(responseCode = "200", description = "Balance showed successfully")
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@AuthenticationPrincipal User user, @PathVariable Long id){
        return ResponseEntity.ok(cardService.getCardBalance(user.getUsername(), id));
    }

    @Operation(summary = "Request block card", description = "This operation should set status BLOCKED_REQUEST")
    @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "This parameter is card id")
    @ApiResponse(responseCode = "200", description = "Card edit status successfully")
    @PostMapping("/{id}/block")
    public ResponseEntity<String> requestBlock(@AuthenticationPrincipal User user, @PathVariable Long id){
        return ResponseEntity.ok(cardService.requestBlockCard(user.getUsername(), id));
    }

    @Operation(summary = "Transfer money", description = "This operation should create money transfer")
    @ApiResponse(responseCode = "200", description = "Transfer created successfully")
    @PostMapping("/transfer")
    public ResponseEntity<String> makeTransfer(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody TransferRequest transferRequest){

        return ResponseEntity.ok(transferService.transfer(user.getUsername(), transferRequest));
    }

}
