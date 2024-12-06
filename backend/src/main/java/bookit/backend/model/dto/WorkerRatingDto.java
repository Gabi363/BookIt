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
public class WorkerRatingDto implements Serializable {

    private long id;
    private long clientId;
    private int grade;
    private String comment;
    private long workerId;
}
