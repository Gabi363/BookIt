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
public class ServiceDto implements Serializable {

    private int id;
    private long businessId;
    private String name;
    private String description;
    private Double price;
    private String category;
}
