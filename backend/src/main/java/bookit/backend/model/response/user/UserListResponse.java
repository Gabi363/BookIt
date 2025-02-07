package bookit.backend.model.response.user;

import bookit.backend.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {

    private List<UserDto> userDtoList;
}
