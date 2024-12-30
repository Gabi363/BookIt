package bookit.backend.model.entity;

import bookit.backend.model.enums.WeekDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "business_working_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessWorkingHours implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "business_working_hours_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_working_hours_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
    private Business business;

    @Column(name = "week_day", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}
