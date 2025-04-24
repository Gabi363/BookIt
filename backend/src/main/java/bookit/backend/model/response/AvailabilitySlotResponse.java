package bookit.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AvailabilitySlotResponse {
    Long id;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
}