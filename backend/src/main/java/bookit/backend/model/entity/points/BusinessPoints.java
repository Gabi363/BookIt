package bookit.backend.model.entity.points;

import bookit.backend.model.entity.Business;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "business_points")
@Data
@NoArgsConstructor
@SuperBuilder
public class BusinessPoints extends LoyalPoints {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}
