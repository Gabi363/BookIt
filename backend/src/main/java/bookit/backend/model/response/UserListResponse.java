package bookit.backend.model.response;

import bookit.backend.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {

    private List<UserDto> userDtoList;
}
