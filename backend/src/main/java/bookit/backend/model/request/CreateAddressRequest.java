package bookit.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAddressRequest {

    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String localNumber;
    @NotBlank
    private String postCode;
}
