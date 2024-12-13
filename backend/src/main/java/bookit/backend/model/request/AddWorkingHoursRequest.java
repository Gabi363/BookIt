package bookit.backend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AddWorkingHoursRequest {

    @NotNull
    List<WorkingHoursDayRequest> workingHoursList;
}
