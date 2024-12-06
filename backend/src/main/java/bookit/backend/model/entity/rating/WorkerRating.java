package bookit.backend.model.entity.rating;

import bookit.backend.model.entity.user.WorkerUser;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "worker_ratings")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "worker_user_id", nullable = false)
    private WorkerUser worker;
}
