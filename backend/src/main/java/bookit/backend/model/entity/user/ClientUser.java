package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Calendar;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.points.ClientPoints;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "client_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class ClientUser extends User {

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Calendar calendar;

    @OneToMany(mappedBy = "client")
    private List<ClientPoints> points;

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservations;
}
