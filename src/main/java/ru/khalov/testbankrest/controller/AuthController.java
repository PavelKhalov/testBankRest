package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registration", description = "This operation should registered new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New user registered successfully")
            //@ApiResponse(...) Юзернейм уже занят
    })
    @PostMapping("/reg")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Login", description = "This operation should login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User login successfully")
            //@ApiResponse(...) Неправильный логин/пароль
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
