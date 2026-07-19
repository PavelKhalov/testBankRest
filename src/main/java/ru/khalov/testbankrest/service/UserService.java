package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.dto.request.UpdateUserRoleRequest;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.UserNotFoundException;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.service.mapper.UserMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(Pageable pageable){
        log.info("Called method 'getAllUsers' from UserService");
        return PageResponse.from(userRepository.findAll(pageable).map(userMapper::toResponse));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse setEnabled(String username, boolean enabled){
        log.info("Called method 'setEnabled' in UserService");
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with username: " + username + " not found"));
        user.setEnabled(enabled);

        return userMapper.toResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse updateRole(String username, UpdateUserRoleRequest request){
        log.info("Called method 'updateRole' in UserService");
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with id: " + username + " not found"));
        user.setRole(request.role());

        return userMapper.toResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String deleteUser(String username){
        log.info("Called method 'deleteUser' in UserService");
        if(!userRepository.existsByUsername(username)){
            throw new UserNotFoundException("User with username: " + username + " not found");
        }
        userRepository.deleteByUsername(username);
        return "User deleted successfully";
    }


}
