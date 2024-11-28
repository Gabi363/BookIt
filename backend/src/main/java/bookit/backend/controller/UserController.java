package bookit.backend.controller;

import bookit.backend.model.response.UserListResponse;
import bookit.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ManagedOperation(description = "Get list of all users")
    public UserListResponse getUsers() {
        return new UserListResponse(userService.getUsers());
    }

}