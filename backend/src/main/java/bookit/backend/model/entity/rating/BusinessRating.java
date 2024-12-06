package bookit.backend.model.entity.rating;

import bookit.backend.model.entity.Business;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "business_ratings")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}
