package bookit.backend.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateServiceRequest {

    @NotNull
    private long businessId;
    @NotBlank
    private String name;
    @Nullable
    private String description;
    @NotNull
    private Double price;
    @Nullable
    private String category;
    @NotNull
    private Double duration;
}
