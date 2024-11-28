package bookit.backend.model.entity.rating;

import bookit.backend.model.entity.user.ClientUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Rating implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "ratings_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ratings_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientUser client;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "comment")
    private String comment;
}
