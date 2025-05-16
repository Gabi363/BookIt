package bookit.backend.model.entity;

import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservartions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "reservation_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_user_id", nullable = false)
    private ClientUser client;

    @ManyToOne
    @JoinColumn(name = "worker_user_id", nullable = false)
    private WorkerUser worker;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Formula(value = "(SELECT s.duration FROM service s WHERE s.id=service_id)")
    private Double duration;

    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

}
