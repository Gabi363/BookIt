package bookit.backend.model.entity.points;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LoyalPoints implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "points_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "points_id_seq")
    private Long id;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "points_number")
    private int pointsNumber;

}
