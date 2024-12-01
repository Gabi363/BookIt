package bookit.backend.model.entity;

import bookit.backend.model.entity.points.BusinessPoints;
import bookit.backend.model.entity.rating.BusinessRating;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.enums.BusinessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "business")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "business_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BusinessType type;

    @Column(name = "contact_phone")
    private String phoneNumber;

    @Column(name = "contact_email")
    private String email;

    @OneToMany(mappedBy = "business")
    private List<Service> services;

    @OneToMany(mappedBy = "business")
    private List<WorkerUser> workers;

    @OneToMany(mappedBy = "business")
    private List<BusinessRating> ratings;

    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BusinessAddress address;

    @OneToMany(mappedBy = "business")
    private List<BusinessPoints> points;
}
