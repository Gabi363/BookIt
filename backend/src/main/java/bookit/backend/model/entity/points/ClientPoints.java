package bookit.backend.model.entity.points;

import bookit.backend.model.entity.user.ClientUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_points")
@Data
public class ClientPoints extends LoyalPoints {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientUser client;
}
