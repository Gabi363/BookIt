package bookit.backend.model.entity;

import bookit.backend.model.entity.points.BusinessPoints;
import bookit.backend.model.entity.rating.BusinessRating;
import bookit.backend.model.entity.rating.Rating;
import bookit.backend.model.entity.user.BusinessOwnerUser;
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

    @OneToOne(mappedBy = "business")
    private BusinessOwnerUser owner;

    @Column(name = "name", unique = true)
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

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<WorkerUser> workers;

    @OneToMany(mappedBy = "business")
    private List<BusinessRating> ratings;

    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BusinessAddress address;

    @OneToOne(mappedBy = "business")
    private BusinessPoints points;

    @OneToMany(mappedBy = "business")
    private List<BusinessWorkingHours> workingHours;

    @OneToMany(mappedBy = "business")
    private List<Reservation> reservations;

    @Transient
    private Double averageRating = getAverageRating();

    public Double getAverageRating() {
        return ratings != null
                ? ratings.stream().mapToDouble(Rating::getGrade).average().orElse(0)
                : 0d;
    }
}
