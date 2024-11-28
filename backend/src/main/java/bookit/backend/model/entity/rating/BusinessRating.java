package bookit.backend.model.entity.rating;

import bookit.backend.model.entity.Business;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "business_ratings")
@Data
public class BusinessRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}
