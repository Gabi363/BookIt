package bookit.backend.model.request;

import bookit.backend.model.enums.WeekDay;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class WorkingHoursDayRequest {

    @NotNull
    private WeekDay weekDay;
    @NotNull
    private Boolean isOpen;
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String startTime;
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String endTime;
}
