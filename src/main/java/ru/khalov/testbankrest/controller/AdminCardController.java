package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khalov.testbankrest.dto.request.CreateCardRequest;
import ru.khalov.testbankrest.dto.response.CardResponse;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.service.CardService;
import ru.khalov.testbankrest.util.CardStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cards")
@Tag(name = "Admin cards controller", description = "Managing all cards (only Admin)")
public class AdminCardController {

    private final CardService cardService;

    @Operation(summary = "Creation card", description = "This operation should create card for user")
    @ApiResponse(responseCode = "200", description = "Card creating successfully")
    @PostMapping("/create")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CreateCardRequest request){
        return ResponseEntity.ok(cardService.createCard(request));
    }

    @Operation(summary = "Get all cards", description = "This operation should show all cards")
    @ApiResponse(responseCode = "200", description = "Cards show successfully")
    @GetMapping()
    public ResponseEntity<PageResponse<CardResponse>> getAllCards(@RequestParam (required = false) String ownerUsername,
                                                                  @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(cardService.getAllCards(ownerUsername, pageable));
    }

    @Operation(summary = "Block card", description = "This operation should block user card")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "This parameter card id")
    @ApiResponse(responseCode = "200", description = "Card blocked successfully")
    @PostMapping("/{id}/block")
    public ResponseEntity<CardResponse> blockCrd(@PathVariable Long id){
        return ResponseEntity.ok(cardService.setStatus(id, CardStatus.BLOCKED));
    }

    @Operation(summary = "Activate card", description = "This operation should activate user card")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "This parameter card id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card activate successfully")
            //@ApiResponse(...) тут по хорошему надо описать другие коды ответов, но в тз небыло такого задания
    })
    @PostMapping("/{id}/acive")
    public ResponseEntity<CardResponse> activateCard(@PathVariable Long id){
        return ResponseEntity.ok(cardService.setStatus(id, CardStatus.ACTIVE));
    }

    @Operation(summary = "Delete card", description = "This operation should delete card")
    @ApiResponse(responseCode = "200", description = "Card deleted successfully")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteCard(@PathVariable Long id){
        return ResponseEntity.ok(cardService.deleteCard(id));
    }
}
