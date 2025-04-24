package bookit.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddAvailabilityRequest {
    @NotNull
    Long workerId;
    @NotBlank
    String date;
    @NotBlank
    String startHour;
    @NotBlank
    String endHour;
}
