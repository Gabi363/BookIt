package bookit.backend.model.response;

import bookit.backend.model.dto.user.WorkerUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReservationSlotsResponse {
    WorkerUserDto worker;
    List<LocalTime> slots;
}
