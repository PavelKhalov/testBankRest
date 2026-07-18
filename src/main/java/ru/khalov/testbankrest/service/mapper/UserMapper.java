package ru.khalov.testbankrest.service.mapper;

import org.springframework.stereotype.Component;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }

}
