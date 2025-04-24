package bookit.backend.model.dto;

import bookit.backend.model.dto.user.WorkerUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityDto implements Serializable {

    private Long id;
    private BusinessDto business;
    private WorkerUserDto worker;
    private LocalDate date;
    private LocalTime startHour;
    private LocalTime endHour;
}
