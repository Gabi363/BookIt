package bookit.backend.model.response.user;

import bookit.backend.model.dto.BusinessOwnerUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessOwnerUserResponse {
    private BusinessOwnerUserDto businessOwner;
}
