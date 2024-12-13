package bookit.backend.model.response;

import bookit.backend.model.dto.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
@AllArgsConstructor
public class ReservationOptionsResponse {

    private ServiceDto service;
    private List<SimpleDateFormat> slots;
}
