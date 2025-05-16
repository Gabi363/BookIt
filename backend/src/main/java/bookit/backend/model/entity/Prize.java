package bookit.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "prizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prize {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "prize_id_seq", allocationSize = 1  )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prize_id_seq")
    private Long id;

    @Column(name = "prize_name", nullable = false)
    String prizeName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "price", nullable = false)
    Long price;

    @Column(name = "points_threshold", nullable = false)
    Long pointsThreshold;

    @OneToMany(mappedBy = "prize")
    private List<UserPrize> userPrizes;
}
