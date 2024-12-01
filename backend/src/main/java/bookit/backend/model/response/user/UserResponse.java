package bookit.backend.model.response.user;

import bookit.backend.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private UserDto user;
}
