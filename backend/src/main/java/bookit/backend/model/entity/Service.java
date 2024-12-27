package bookit.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "service_id_seq", allocationSize = 1  )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "service_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private Double duration;

    @OneToMany(mappedBy = "service")
    private List<Reservation> reservations;
}