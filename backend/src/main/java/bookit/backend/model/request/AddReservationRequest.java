package bookit.backend.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddReservationRequest {

    @NotNull
    String date;
    @Nullable
    Long workerId;
}
