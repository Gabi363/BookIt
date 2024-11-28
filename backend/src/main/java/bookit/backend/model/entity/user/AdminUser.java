package bookit.backend.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class AdminUser extends User {
}
