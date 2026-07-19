package ru.khalov.testbankrest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khalov.testbankrest.dto.request.UpdateUserRoleRequest;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.service.UserService;
import ru.khalov.testbankrest.util.Role;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    private UserResponse userResponse;

    @Test
    void getAllUsersTest() {
        Pageable pageable = PageRequest.of(0, 5);
        userResponse = new UserResponse("john", "John", "Doe", "john@mail.com", Role.USER, true);
        PageResponse<UserResponse> page = new PageResponse<>(List.of(userResponse), 0, 5, 1, 1, true);

        when(userService.getAllUsers(pageable)).thenReturn(page);

        ResponseEntity<PageResponse<UserResponse>> response = adminUserController.getAllUsers(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void updateRoleTest() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(Role.ADMIN);
        userResponse = new UserResponse( "john", "John", "Doe", "john@mail.com", Role.ADMIN, true);

        when(userService.updateRole("john", request)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = adminUserController.updateRole("john", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void enableUserTest() {
        userResponse = new UserResponse( "john", "John", "Doe", "john@mail.com", Role.USER, true);

        when(userService.setEnabled("john", true)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = adminUserController.enableUser("john");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void disableUserTest() {
        userResponse = new UserResponse( "john", "John", "Doe", "john@mail.com", Role.USER, false);

        when(userService.setEnabled("john", false)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = adminUserController.disableUser("john");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void deleteUserTest() {
        when(userService.deleteUser("john")).thenReturn("User deleted successfully");

        ResponseEntity<String> response = adminUserController.deleteUser("john");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
    }
}
