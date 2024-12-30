package bookit.backend.model.dto.user;

import bookit.backend.model.dto.WorkerRatingDto;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerUserDto extends UserDto {

    private List<WorkerRatingDto> ratings;
    private Double averageRating;
}
