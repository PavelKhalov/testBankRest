package ru.khalov.testbankrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all users", description = "This operation should show all users")
    @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "This parameter is users username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users showed successfully")
            // @ApiResponse(...) тут допустим обработка того, что нет ни одного пользователя
    })
    @GetMapping()
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(@PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Update user role", description = "This operation should update user role")
    @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "This parameter is users username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role was updates successfully")
            //@ApiResponse(...) тут допустим обработка что конкретный пользователь не найден
    })
    @PostMapping("/{username}/role")
    public ResponseEntity<UserResponse> updateRole(@PathVariable String username,
                                                   @Valid @RequestBody UpdateUserRoleRequest request){
        return ResponseEntity.ok(userService.updateRole(username, request));
    }

    @Operation(summary = "Enable user", description = "This operation should set enable status")
    @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "This parameter is users username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User enabled successfully")
            //@ApiResponse(...) //так же допустим, что пользователь не найден
    })
    @PostMapping("/{username}/enable")
    public ResponseEntity<UserResponse> enableUser(@PathVariable String username){
        return ResponseEntity.ok(userService.setEnabled(username, true));
    }

    @Operation(summary = "Disable user", description = "This operation should set disable status")
    @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "This parameter is users username")
    @ApiResponse(responseCode = "200", description = "User disabled successfully")
    @PostMapping("/{username}/disable")
    public ResponseEntity<UserResponse> disableUser(@PathVariable String username){
        return ResponseEntity.ok(userService.setEnabled(username, false));
    }

    @Operation(summary = "Delete user", description = "This operation should delete user")
    @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "This parameter is users username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully")
            //@ApiResponce(...) пользователь не найден
    })
    @DeleteMapping("/{username}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
