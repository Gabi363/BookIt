package bookit.backend.model.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_owner_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class BusinessOwnerUser extends User {

    @Column(name = "nip")
    private String nip;
}
