package bookit.backend.model.request;

import bookit.backend.model.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    private String password;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private UserRole userRole;
    @Nullable
    private String nip;
}
