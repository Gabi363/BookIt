package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Calendar;
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

    @OneToMany(mappedBy = "worker")
    private List<WorkerRating> workerRatings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Calendar calendar;
}
