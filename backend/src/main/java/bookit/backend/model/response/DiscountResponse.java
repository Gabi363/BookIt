package bookit.backend.model.response;

import bookit.backend.model.dto.UserPrizeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DiscountResponse {
    List<UserPrizeDto> discounts;
}
