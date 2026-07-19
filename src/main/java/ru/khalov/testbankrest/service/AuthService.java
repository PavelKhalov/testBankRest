package ru.khalov.testbankrest.service;

import jdk.jfr.StackTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.dto.request.LoginRequest;
import ru.khalov.testbankrest.dto.request.RegisterRequest;
import ru.khalov.testbankrest.dto.response.AuthResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.UserNotFoundException;
import ru.khalov.testbankrest.exception.UsernameAlreadyExistsException;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.util.Role;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.username())){
            throw new UsernameAlreadyExistsException(request.username());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());
        user.setCards(new ArrayList<>());
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer ", request.username(), Role.USER.name());
    }

    @Transactional
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        User user = userRepository.findByUsername(request.username()).orElseThrow(()->
                new UserNotFoundException("User with username: " + request.username() + " not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer ", request.username(), user.getRole().name());
    }
}
