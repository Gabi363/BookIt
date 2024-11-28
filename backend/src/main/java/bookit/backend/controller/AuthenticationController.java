package bookit.backend.controller;
import bookit.backend.model.dto.UserDto;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.LoginRequest;
import bookit.backend.model.response.LoginResponse;
import bookit.backend.model.response.UserResponse;
import bookit.backend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
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
    //    private final JwtService jwtService;
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
//        String token = jwtService.generateToken(user.get().getId());
        String token = "lll";
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, user.get()));
    }
}