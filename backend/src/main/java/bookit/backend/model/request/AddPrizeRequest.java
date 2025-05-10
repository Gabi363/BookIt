package bookit.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddPrizeRequest {
    @NotBlank
    String prizeName;
    @NotBlank
    String description;
    @NotBlank
    Long price;
    @NotNull
    Long pointsThreshold;
}
