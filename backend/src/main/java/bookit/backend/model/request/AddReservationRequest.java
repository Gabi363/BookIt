package bookit.backend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddReservationRequest {

    @NotNull
    String date;
    @NotNull
    Long workerId;
}
