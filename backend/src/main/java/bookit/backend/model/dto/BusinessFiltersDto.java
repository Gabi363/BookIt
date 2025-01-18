package bookit.backend.model.dto;

import bookit.backend.model.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessFiltersDto {
    private BusinessType businessType;
    private String city;
    private Double minimumRating;
}
