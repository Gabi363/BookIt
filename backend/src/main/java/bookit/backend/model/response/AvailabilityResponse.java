package bookit.backend.model.response;

import bookit.backend.model.dto.user.WorkerUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailabilityResponse {
    WorkerUserDto worker;
    List<AvailabilitySlotResponse> slots;
}
