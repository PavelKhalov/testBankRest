package ru.khalov.testbankrest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.khalov.testbankrest.dto.request.UpdateUserRoleRequest;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.repository.UserRepository;
import ru.khalov.testbankrest.service.mapper.UserMapper;
import ru.khalov.testbankrest.util.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("john")
                .name("John")
                .surname("Doe")
                .email("john@mail.com")
                .role(Role.USER)
                .enabled(true)
                .build();

        userResponse = new UserResponse( "john", "John", "Doe", "john@mail.com", Role.USER, true);
    }

    @Test
    void getAllUsersTest() {
        Pageable pageable = PageRequest.of(0, 5);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user), pageable, 1));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        PageResponse<UserResponse> result = userService.getAllUsers(pageable);

        assertEquals(1, result.content().size());
        assertEquals(userResponse, result.content().get(0));
    }

    @Test
    void setEnabledTest() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.setEnabled("john", false);

        assertEquals(false, user.isEnabled());
        assertEquals(userResponse, response);
    }

    @Test
    void updateRoleTest() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(Role.ADMIN);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        userService.updateRole("john", request);

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void deleteUserTest() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        String result = userService.deleteUser("john");

        verify(userRepository).deleteByUsername("john");
        assertTrue(result.contains("deleted"));
    }
}
