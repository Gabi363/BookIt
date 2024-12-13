package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.Calendar;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.rating.Rating;
import bookit.backend.model.entity.rating.WorkerRating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "worker_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class WorkerUser extends User {

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkerRating> workerRatings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Calendar calendar;

    @OneToMany(mappedBy = "worker")
    private List<Reservation> reservations;

    @Transient
    private Double averageRating = getAverageRating();

    public Double getAverageRating() {
        return workerRatings != null
                ? workerRatings.stream().mapToDouble(Rating::getGrade).average().orElse(0)
                : 0d;
    }
}
