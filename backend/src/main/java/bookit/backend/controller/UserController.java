package bookit.backend.controller;

import bookit.backend.model.dto.user.UserDto;
import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.DeleteUserRequest;
import bookit.backend.model.response.user.BusinessOwnerUserResponse;
import bookit.backend.model.response.user.UserListResponse;
import bookit.backend.model.response.user.UserResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ManagedOperation(description = "Get list of all users")
    public UserListResponse getUsers() {
        return new UserListResponse(userService.getUsers());
    }

    @GetMapping("/current")
    @ManagedOperation(description = "Get current user's information")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role == UserRole.ADMIN) {
            var user = userService.getAdminUserById(userId);
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }
        else if(role == UserRole.BUSINESS_OWNER) {
            var user = userService.getBusinessOwnerUserById(userId);
            if(user.isPresent()) return ResponseEntity.ok().body(new BusinessOwnerUserResponse(user.get()));
        }
        else if(role == UserRole.WORKER) {
            var user = userService.getWorkerUserById(userId);
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }
        else if(role == UserRole.CLIENT) {
            var user = userService.getClientUserById(userId);
            if(user.isPresent()) return ResponseEntity.ok().body(new UserResponse(user.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist!");
    }

    @PostMapping("/{userId}")
    @ManagedOperation(description = "Update user's information")
    public ResponseEntity<?> updateUser(@Valid @RequestBody CreateUserRequest request,
                                        @PathVariable long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long currentId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && userId != currentId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var oldRole = userService.getUserRole(userId).orElse(null);
        if(oldRole == null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Account does not exist!");
        if(userId == currentId) request.setUserRole(null);

        Optional<UserDto> updatedUser = accountService.updateUser(request, userId, oldRole);

        if(updatedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account does not exist!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/delete/{userId}")
    @ManagedOperation(description = "Delete user")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserRequest request,
                                        @PathVariable long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long currentId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && userId != currentId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = accountService.deleteUserByEmail(request.getEmail());
        return ResponseEntity.status(status).build();
    }
}