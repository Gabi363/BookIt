package bookit.backend.model.entity;

import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "business_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_user_id", nullable = false)
    private ClientUser clientUser;

    @ManyToOne
    @JoinColumn(name = "worker_user_id", nullable = false)
    private WorkerUser workerUser;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "business", nullable = false)
    private Business business;

    @Column(name = "checked")
    @ColumnDefault("false")
    private Boolean checked;
}
