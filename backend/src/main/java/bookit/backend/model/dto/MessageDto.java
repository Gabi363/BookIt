package bookit.backend.model.dto;

import bookit.backend.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto implements Serializable {

    private Long id;
    private LocalDateTime date;
    private String body;
    private UserDto sender;
    private UserDto receiver;
}
