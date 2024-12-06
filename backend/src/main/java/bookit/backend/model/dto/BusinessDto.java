package bookit.backend.model.dto;

import bookit.backend.model.dto.user.WorkerUserDto;
import bookit.backend.model.entity.points.BusinessPoints;
import bookit.backend.model.entity.rating.BusinessRating;
import bookit.backend.model.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDto implements Serializable {

    private Long id;
    private String name;
    private BusinessType type;
    private String phoneNumber;
    private String email;
    private List<ServiceDto> services;
    private List<WorkerUserDto> workers;
    private List<BusinessRating> ratings;
    private BusinessAddressDto address;
    private List<BusinessPoints> points;
}
