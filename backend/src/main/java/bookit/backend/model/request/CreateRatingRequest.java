package bookit.backend.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRatingRequest {

    @NotNull
    private int grade;
    @Nullable
    private String comment;
}
