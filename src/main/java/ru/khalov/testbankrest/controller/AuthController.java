package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khalov.testbankrest.dto.request.LoginRequest;
import ru.khalov.testbankrest.dto.request.RegisterRequest;
import ru.khalov.testbankrest.dto.response.AuthResponse;
import ru.khalov.testbankrest.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registration and login (give JWT)")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/reg")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
