package ru.khalov.testbankrest.service;

import jdk.jfr.StackTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.dto.UserDto;
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
public class UserService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String saveUser(UserDto userDto) {
        log.info("Called method: 'saveUser' from UserService");

        User userEntity = new User();
        userEntity.setPassword(passwordEncoder.encode(userDto.password()));
        userEntity.setName(userDto.name());
        userEntity.setSurname(userDto.surname());
        userEntity.setEmail(userDto.email());
        userEntity.setRoles(Set.of(Role.USER));

        log.info("User created successfully");

        try{
            log.info("Start save user in db");
            userRepository.saveAndFlush(userEntity);
            return "User save successfully!";
        } catch (Exception e){
            log.error("Error to save in db: {}", e.getMessage());
            throw new UserNotCreatedException("User dont created");
        }
    }

    @Transactional
    public String deleteUser(UserDto userDto){
        log.info("Called method: 'deleteUser' from UserServiceImpl");
        User user = userRepository.findByUsername(userDto.username()).orElseThrow(() ->
                new UsernameNotFoundException("User with username: "  + userDto.username() + ", not found"));

        userRepository.deleteByUsername(userDto.username());
        return "User delete successfully";
    }

    @Transactional
    public String updateUser(UserDto userDto){
        log.info("Called method: 'updateUser' from UserService");
        User user = userRepository.findByUsername(userDto.username()).orElseThrow(() ->
                new UsernameNotFoundException("User with username: "  + userDto.username() + ", not found"));
        try {
            log.info("start update user");

            user.setName(userDto.name());
            user.setSurname(userDto.surname());
            user.setEmail(userDto.email());

            if(userDto.cardIds() != null) {
                List<Card> cards = cardRepository.findAllById(userDto.cardIds());
                user.setCards(cards);
            }

            log.info("user update successfully");
            return "User update successfully";
        } catch (Exception e){
            log.error("Error to update user: {}", e.getMessage());
            return "Error to update user!";
        }
    }

}
