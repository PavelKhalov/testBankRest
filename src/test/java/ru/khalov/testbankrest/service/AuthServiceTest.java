package ru.khalov.testbankrest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.khalov.testbankrest.dto.request.LoginRequest;
import ru.khalov.testbankrest.dto.request.RegisterRequest;
import ru.khalov.testbankrest.dto.response.AuthResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.util.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerTest() {
        RegisterRequest request = new RegisterRequest("john", "pass123", "John", "Doe", "john@mail.com");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        assertEquals("john", response.username());
        assertEquals(Role.USER.name(), response.role());
    }

    @Test
    void loginTest() {
        LoginRequest request = new LoginRequest("john", "pass123");

        User user = User.builder()
                .username("john")
                .password("encodedPass")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.token());
        assertEquals("john", response.username());
        assertEquals(Role.USER.name(), response.role());
    }
}
