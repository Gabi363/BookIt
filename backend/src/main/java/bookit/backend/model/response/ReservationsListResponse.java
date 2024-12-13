package bookit.backend.model.response;

import bookit.backend.model.dto.ReservationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReservationsListResponse {

    private List<ReservationDto> reservations;
}
