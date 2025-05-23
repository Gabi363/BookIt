package bookit.backend.controller;

import bookit.backend.model.dto.user.UserDto;
import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.LoginRequest;
import bookit.backend.model.request.RefreshTokenRequest;
import bookit.backend.model.response.LoginResponse;
import bookit.backend.model.response.user.UserResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.JwtService;
import bookit.backend.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {

    private final AccountService accountService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("register/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create new admin account")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody CreateUserRequest request) {
        request.setUserRole(UserRole.ADMIN);
        Optional<UserDto> user = accountService.createUser(request);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with given e-mail already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.get()));
    }

    @PostMapping("register/client")
    @Operation(summary = "Create new user account")
    public ResponseEntity<?> createClientUser(@Valid @RequestBody CreateUserRequest request) {
        request.setUserRole(UserRole.CLIENT);
        Optional<UserDto> user = accountService.createUser(request);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with given e-mail already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.get()));
    }

    @PostMapping("register/business")
    @Operation(summary = "Create new business owner account")
    public ResponseEntity<?> createBusinessOwnerUser(@Valid @RequestBody CreateUserRequest request) {
        request.setUserRole(UserRole.BUSINESS_OWNER);
        Optional<UserDto> user = accountService.createUser(request);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with given e-mail already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.get()));
    }

    @PostMapping("login")
    @Operation(summary = "Login to account")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<UserDto> user = accountService.login(request);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong e-mail or password!");
        }
        String token = jwtService.generateToken(user.get().getEmail(), user.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, user.get()));
    }

    @PostMapping("refresh")
    @Operation(summary = "Refresh jwt token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        UserDto user;
        String email = "";
        try {
            jwtService.extractUsername(request.getToken());
        } catch (ExpiredJwtException e) {
            email = e.getClaims().getSubject();
        }

        try {
            user = userService.getUserByEmail(email).orElseThrow();
        } catch (Exception ignored) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account does not exist!");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, user));
    }
}