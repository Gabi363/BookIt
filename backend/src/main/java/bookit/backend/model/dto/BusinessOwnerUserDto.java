package bookit.backend.model.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessOwnerUserDto extends UserDto {

    private String nip;
}
