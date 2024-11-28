package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Calendar;
import bookit.backend.model.entity.points.ClientPoints;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "client_users")
@Data
public class ClientUser extends User {

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Calendar calendar;

    @OneToMany(mappedBy = "client")
    private List<ClientPoints> points;
}
