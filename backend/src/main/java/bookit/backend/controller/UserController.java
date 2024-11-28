package bookit.backend.controller;

import bookit.backend.model.dto.UserDto;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.LoginRequest;
import bookit.backend.model.response.LoginResponse;
import bookit.backend.model.response.UserListResponse;
import bookit.backend.model.response.UserResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @GetMapping
    @ManagedOperation(description = "Get list of all users")
    public UserListResponse getUsers() {
        return new UserListResponse(userService.getUsers());
    }

    @PostMapping("register")
    @ManagedOperation(description = "Create new user account")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        Optional<UserDto> user = accountService.createUser(request);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with given e-mail already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.get()));
    }

    @PostMapping("login")
    @ManagedOperation(description = "Login to account")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<UserDto> user = accountService.login(request);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong e-mail or password!");
        }
        String token = UUID.randomUUID().toString();
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, user.get()));
    }
}