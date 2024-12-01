package bookit.backend.model.response;

import bookit.backend.model.dto.BusinessDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessResponse {

    private BusinessDto businessDto;
}
