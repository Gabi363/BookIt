package bookit.backend.model.entity.points;

import bookit.backend.model.entity.Business;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "business_points")
@Data
public class BusinessPoints extends LoyalPoints {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}
