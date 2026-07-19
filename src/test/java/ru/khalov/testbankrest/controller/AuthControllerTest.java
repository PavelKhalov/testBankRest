package ru.khalov.testbankrest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khalov.testbankrest.dto.request.LoginRequest;
import ru.khalov.testbankrest.dto.request.RegisterRequest;
import ru.khalov.testbankrest.dto.response.AuthResponse;
import ru.khalov.testbankrest.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerTest() {
        RegisterRequest request = new RegisterRequest("john", "pass123", "John", "Doe", "john@mail.com");
        AuthResponse expected = new AuthResponse("jwt-token", "Bearer ", "john", "USER");

        when(authService.register(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void loginTest() {
        LoginRequest request = new LoginRequest("john", "pass123");
        AuthResponse expected = new AuthResponse("jwt-token", "Bearer ", "john", "USER");

        when(authService.login(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}
