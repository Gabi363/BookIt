package bookit.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrizeDto implements Serializable {

    private Long id;
    String prizeName;
    String description;
    Long price;
    Long pointsThreshold;
}
