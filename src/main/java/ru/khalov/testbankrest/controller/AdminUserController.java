package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khalov.testbankrest.dto.request.UpdateUserRoleRequest;
import ru.khalov.testbankrest.dto.response.PageResponse;
import ru.khalov.testbankrest.dto.response.UserResponse;
import ru.khalov.testbankrest.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin user controller", description = "Manage all users (only Admin)")
public class AdminUserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(@PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PostMapping("/{username}/role")
    public ResponseEntity<UserResponse> updateRole(@PathVariable String username,
                                                   @Valid @RequestBody UpdateUserRoleRequest request){
        return ResponseEntity.ok(userService.updateRole(username, request));
    }

    @PostMapping("/{username}/enable")
    public ResponseEntity<UserResponse> enableUser(@PathVariable String username){
        return ResponseEntity.ok(userService.setEnabled(username, true));
    }

    @PostMapping("/{username}/disable")
    public ResponseEntity<UserResponse> disableUser(@PathVariable String username){
        return ResponseEntity.ok(userService.setEnabled(username, false));
    }

    @DeleteMapping("/{username}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
