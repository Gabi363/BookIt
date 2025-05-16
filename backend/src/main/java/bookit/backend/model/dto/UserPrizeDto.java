package bookit.backend.model.dto;

import bookit.backend.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrizeDto implements Serializable {

    private Long id;
    private UserDto user;
    private PrizeDto prize;
    private boolean used;
    private String discountCode;
}
