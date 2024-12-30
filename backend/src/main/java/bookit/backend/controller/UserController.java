package bookit.backend.controller;

import bookit.backend.model.dto.user.UserDto;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.DeleteUserRequest;
import bookit.backend.model.response.user.BusinessOwnerUserResponse;
import bookit.backend.model.response.user.UserListResponse;
import bookit.backend.model.response.user.UserResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get list of all users")
    public UserListResponse getUsers() {
        return new UserListResponse(userService.getUsers());
    }

    @GetMapping("/current")
    @Operation(summary = "Get current user's information")
    public ResponseEntity<?> getCurrentUser() {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();

        if(!userInfo.isNotAdmin()) {
            var user = userService.getAdminUserById(userInfo.getId());
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }
        else if(!userInfo.isNotBusinessOwner()) {
            var user = userService.getBusinessOwnerUserById(userInfo.getId());
            if(user.isPresent()) return ResponseEntity.ok().body(new BusinessOwnerUserResponse(user.get()));
        }
        else if(userInfo.isWorker()) {
            var user = userService.getWorkerUserById(userInfo.getId());
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }
        else if(userInfo.isClient()) {
            var user = userService.getClientUserById(userInfo.getId());
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist!");
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Update user's information")
    public ResponseEntity<?> updateUser(@Valid @RequestBody CreateUserRequest request,
                                        @PathVariable long userId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotAdmin() && userId != userInfo.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var oldRole = userService.getUserRole(userId).orElse(null);
        if(oldRole == null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Account does not exist!");
        if(userId == userInfo.getId()) request.setUserRole(null);

        Optional<UserDto> updatedUser = accountService.updateUser(request, userId, oldRole);

        if(updatedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account does not exist!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserRequest request,
                                        @PathVariable long userId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotAdmin() && userId != userInfo.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = accountService.deleteUserByEmail(request.getEmail());
        return ResponseEntity.status(status).build();
    }
}