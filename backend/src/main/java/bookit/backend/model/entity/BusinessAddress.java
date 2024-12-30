package bookit.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "business_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessAddress implements Serializable {

    @Id
    @Column(name = "business_id")
    private Long business_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "local_number", nullable = false)
    private String localNumber;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @Override
    public String toString() {
        return street + " " + localNumber + ", " + postCode + ", " + city;
    }
}
