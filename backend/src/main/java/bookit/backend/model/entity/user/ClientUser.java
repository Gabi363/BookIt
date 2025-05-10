package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Calendar;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.points.ClientPoints;
import bookit.backend.model.entity.rating.Rating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class ClientUser extends User {

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Calendar calendar;

    @OneToOne(mappedBy = "client")
    private ClientPoints points;

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;
}
