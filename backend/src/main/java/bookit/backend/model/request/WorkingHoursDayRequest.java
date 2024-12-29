package bookit.backend.model.request;

import bookit.backend.model.enums.WeekDay;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WorkingHoursDayRequest {

    @NotNull
    private WeekDay weekDay;
    @NotNull
    private Boolean isOpen;
    @Nullable
    private String startTime;
    @Nullable
    private String endTime;
}
