package bookit.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessAddressDto implements Serializable {

    private Long business_id;
    private String city;
    private String street;
    private String localNumber;
    private String postCode;
}
