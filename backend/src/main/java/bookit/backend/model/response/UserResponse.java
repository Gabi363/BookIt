package bookit.backend.model.response;

import bookit.backend.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private UserDto user;
}
