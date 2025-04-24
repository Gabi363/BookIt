package bookit.backend.model.entity;

import bookit.backend.model.entity.user.WorkerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "availability_id_seq", allocationSize = 1  )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "worker_user_id", nullable = false)
    private WorkerUser worker;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_hour", nullable = false)
    private LocalTime startHour;

    @Column(name = "end_hour", nullable = false)
    private LocalTime endHour;
}