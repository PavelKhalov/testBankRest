package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping("/create")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CreateCardRequest request){
        return ResponseEntity.ok(cardService.createCard(request));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<CardResponse>> getAllCards(@RequestParam (required = false) String ownerUsername,
                                                                  @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(cardService.getAllCards(ownerUsername, pageable));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<CardResponse> blockCrd(@PathVariable Long id){
        return ResponseEntity.ok(cardService.setStatus(id, CardStatus.BLOCKED));
    }

    @PostMapping("/{id}/acive")
    public ResponseEntity<CardResponse> activateCard(@PathVariable Long id){
        return ResponseEntity.ok(cardService.setStatus(id, CardStatus.ACTIVE));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteCard(@PathVariable Long id){
        return ResponseEntity.ok(cardService.deleteCard(id));
    }
}
