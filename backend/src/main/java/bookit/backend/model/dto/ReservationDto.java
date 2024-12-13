package bookit.backend.model.dto;

import bookit.backend.model.dto.user.ClientUserDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto implements Serializable {

    private Long id;
    private ClientUserDto client;
    private WorkerUserDto worker;
    private LocalDateTime date;
    private long businessId;
    private ServiceDto service;
    private Double duration;
}
