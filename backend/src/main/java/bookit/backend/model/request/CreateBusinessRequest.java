package bookit.backend.model.request;

import bookit.backend.model.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateBusinessRequest {

    @NotBlank
    private String name;
    @NotNull
    private BusinessType type;
    @NotBlank
    private String phoneNumber;
    @Email
    private String email;
    @NotNull
    private CreateAddressRequest addressRequest;
}
