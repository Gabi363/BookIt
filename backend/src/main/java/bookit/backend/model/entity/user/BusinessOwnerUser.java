package bookit.backend.model.entity.user;

import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.UserPrize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_owner_users")
@Data
@NoArgsConstructor
@SuperBuilder
public class BusinessOwnerUser extends User {

    @Column(name = "nip")
    private String nip;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

    @OneToMany(mappedBy = "user")
    private List<UserPrize> userPrizes;
}
