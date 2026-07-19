package ru.khalov.testbankrest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.service.UserService;
import ru.khalov.testbankrest.util.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {

    @MockitoBean
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;


    @Test
    void getAllUsersTest(){
        var user1 = new UserResponse(
                "username1",
                "Name1",
                "Surname1",
                "dddff@ya.ru",
                Role.USER,
                true
        );

        var user2 = new UserResponse(
                "username2",
                "Name2",
                "Surname2",
                "dddfddf@ya.ru",
                Role.USER,
                true
        );

        Pageable pageable = PageRequest.of(0, 5);

        PageResponse<UserResponse> page = new PageResponse<>(List.of(user1, user2),
                0,
                5,
                2,
                1,
                true);

        when(userService.getAllUsers(pageable)).thenReturn(page);
        ResponseEntity<PageResponse<UserResponse>> response =  adminUserController.getAllUsers(pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void updateRoleTest(){

    }
}
