package bookit.backend.model.dto;

import bookit.backend.model.dto.user.ClientUserDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public LocalDateTime getEndDate() {
        int hours = duration.intValue();
        int minutes = (int) ((duration - hours) * 60);
        return date.plusHours(hours).plusMinutes(minutes);
    }

    private boolean contains(LocalTime start, LocalTime end) {
        return date.toLocalTime().isBefore(start) && getEndDate().toLocalTime().isAfter(end);
    }

    private boolean isContained(LocalTime start, LocalTime end) {
        return date.toLocalTime().isAfter(start) && getEndDate().toLocalTime().isBefore(end);
    }

    private boolean timesEqual(LocalTime start, LocalTime end) {
        return date.toLocalTime().equals(start) && getEndDate().toLocalTime().equals(end);
    }

    private boolean startsWithinEndsAfter(LocalTime start, LocalTime end) {
        return date.toLocalTime().isAfter(start) && date.toLocalTime().isBefore(end)
                && getEndDate().toLocalTime().isAfter(end);
    }

    private boolean startsBeforeEndsWithin(LocalTime start, LocalTime end) {
        return date.toLocalTime().isBefore(start)
                && getEndDate().toLocalTime().isBefore(end) && getEndDate().toLocalTime().isAfter(start);
    }

    public boolean isOverlapped(LocalTime start, LocalTime end) {
        return this.timesEqual(start, end)
                || this.contains(start, end)
                || this.isContained(start, end)
                || this.startsWithinEndsAfter(start, end)
                || this.startsBeforeEndsWithin(start, end);
    }

}
