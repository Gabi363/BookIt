package bookit.backend.model.entity;

import bookit.backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_prizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrize {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "user_prize_id_seq", allocationSize = 1  )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_prize_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "prize_id")
    private Prize prize;

    @Column(name = "used")
    private boolean used;

    @Column(name = "discount_code")
    private String discountCode;
}
