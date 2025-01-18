package bookit.backend.model.response;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.BusinessFiltersDto;
import bookit.backend.model.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BusinessListAndFiltersResponse {

    private List<BusinessDto> businessDtoList;
    private List<BusinessType> businessTypes;
    private List<String> cities;
}
