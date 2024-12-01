package bookit.backend.model.response;

import bookit.backend.model.dto.BusinessDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BusinessListResponse {

    private List<BusinessDto> businessDtoList;
}
