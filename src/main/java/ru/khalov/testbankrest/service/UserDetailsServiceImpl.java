package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.UserNotCreatedException;
import ru.khalov.testbankrest.repository.CardRepository;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.util.Role;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException{
        log.info("Called method: 'loadUserByUsername' from UserServiceImpl, username = {}", username);
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username: "  + username + ", not found"));
    }

}
