package bookit.backend.model.dto;

import bookit.backend.model.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessWorkingHoursDto implements Serializable {
    private WeekDay weekDay;
    private boolean isOpen;
    private String startTime;
    private String endTime;
}
