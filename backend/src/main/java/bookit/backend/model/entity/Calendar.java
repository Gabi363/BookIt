package bookit.backend.model.entity;

import bookit.backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "calendar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendar implements Serializable {

    @Id
    @Column(name="user_id")
    Long user_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "url")
    private String url;
}
